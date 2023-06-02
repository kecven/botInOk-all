package digital.moveto.botinok.client.linkedin;

import com.microsoft.playwright.Playwright;
import digital.moveto.botinok.client.config.ClientConst;
import digital.moveto.botinok.client.config.GlobalConfig;
import digital.moveto.botinok.client.exeptions.StopBotWorkException;
import digital.moveto.botinok.client.feign.AccountFeignClient;
import digital.moveto.botinok.client.service.ClientAccountService;
import digital.moveto.botinok.client.ui.MainScene;
import digital.moveto.botinok.client.ui.TutorialScene;
import digital.moveto.botinok.client.ui.UiElements;
import digital.moveto.botinok.client.utils.FileUtils;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.enums.SettingKey;
import digital.moveto.botinok.model.service.SettingService;
import digital.moveto.botinok.model.utils.BotinokUtils;
import jakarta.annotation.PostConstruct;
import javafx.scene.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LinkedinBotStarter {
    Logger log = LoggerFactory.getLogger(LinkedinBotStarter.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private ClientAccountService clientAccountService;

    @Autowired
    private UiElements uiElements;

    @Autowired
    private MainScene mainScene;

    @Autowired
    private TutorialScene tutorialScene;

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private SettingService settingService;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void runInThread(Runnable runnable) {
        singleThreadExecutor.submit(runnable);
    }

    private Thread threadIn24Hours = null;


    @PostConstruct
    public void init() {
        addAllAccountToUi();
        if (tutorialScene.getShowTutorial()) {
            tutorialScene.init();
        } else {
            mainScene.finishInitialization();
        }

        Playwright.create().close();

        uiElements.getStartButton().setOnMouseClicked(event -> {
            if (uiElements.getStartButton().getText().equals("Start")) {
                uiElements.changeButtonState(false);

                log.info("Click button start bot");
                if (!uiElements.saveSettingForUser()) {
                    return; // have error in save settings
                }
                start();
            } else {
                log.info("Click button stop bot");
                uiElements.addLogToLogArea("Stop bot");
                uiElements.changeButtonState(true);
                singleThreadExecutor.shutdownNow();
                singleThreadExecutor = Executors.newSingleThreadExecutor();
            }
        });

        uiElements.getStartEvery24Hours().setOnAction(e -> {
            settingService.setSetting(SettingKey.START_AUTOMATICALLY_EVERY_24_HOURS, uiElements.getStartEvery24Hours().isSelected());
            if (uiElements.getStartEvery24Hours().isSelected()) {
                uiElements.getStartEvery24Hours().setText("Start every 24 hours");
            } else {
                uiElements.getStartEvery24Hours().setText("Start only once");
                threadIn24Hours.interrupt();
            }
        });


        uiElements.addLogToLogArea("Loading complete");
        uiElements.changeButtonState(true);
        uiElements.getStartButton().setCursor(Cursor.HAND);

    }

    private void start() {
        threadIn24Hours = new Thread(() -> {
            try {
                Thread.sleep(1000 * 60 * 60 * 24);
                if (uiElements.getStartEvery24Hours().isSelected()) {
                    start();
                }
            } catch (InterruptedException e) {
                return;
            }
        });
        threadIn24Hours.start();

        runInThread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    startSearchConnectsAndConnect();
                }
            } catch (Exception e) {
                Throwable exceptionCause = e;
                for (int i = 0; i < 20; i++) {
                    exceptionCause = exceptionCause.getCause();
                    if (exceptionCause == null) {
                        break;
                    }
                    if (exceptionCause.getClass().equals(InterruptedException.class)) {
                        uiElements.addLogToLogArea("Bot stopped");
                        return;
                    }
                }
                log.error("Error in startSearchConnectsAndConnect", e);
                uiElements.addLogToLogArea("Error in Application. Please, restart application");
            } finally {
                uiElements.changeButtonState(true);
            }
        });
    }

    private void addAllAccountToUi(){
        List<Account> accounts = clientAccountService.findAllActive();
        if (accounts == null || accounts.isEmpty()) {
            // start first time
            tutorialScene.setShowTutorial(true);
            accounts.add(clientAccountService.addNewAccount());

        }
        uiElements.updateAccounts(accounts);
    }

    public void startSearchConnectsAndConnect() {
        uiElements.addLogToLogArea("Initialization bot");
        uiElements.changeButtonState(false);
        log.info("Start LinkedinBotStarter");

        FileUtils.mkdirs(globalConfig.pathToStateFolder);   //if we don't have a folder, we create it

        List<Account> allActiveAccounts = clientAccountService.findAllActive();

        for (int i = 0; i < allActiveAccounts.size(); i++) {
            Account account = clientAccountService.findById(allActiveAccounts.get(i).getId()).get();

            log.info("Start bot for user " + account.getFirstName());

            try (LinkedinBotService linkedinBotService = context.getBean(LinkedinBotService.class)) {

                botWork(linkedinBotService, account);

                if (i != allActiveAccounts.size() - 1) {
                    log.info("Timeout between users " + ClientConst.SLEEP_BETWEEN_START_BOT_FOR_DIFFERENT_USERS);
                    linkedinBotService.sleepRandom(ClientConst.SLEEP_BETWEEN_START_BOT_FOR_DIFFERENT_USERS);
                }
            } catch (StopBotWorkException e) {
                throw e;
            } catch (Exception e) {
                log.error("Error bot for user " + account.getFullName() + ", UUID = " + account.getId(), e);
            }
        }

        log.info("End startSearchConnectsAndConnect");
        uiElements.changeButtonState(true);
        uiElements.addLogToLogArea("Successfully finished");
    }

    private void botWork(LinkedinBotService linkedinBotService, Account account) {

        List<Account> accountList = clientAccountService.findAllActive();
        uiElements.updateAccounts(accountList, account.getId());

        if ( ! account.getActive()){
            log.info("Account " + account.getFullName() + " is not active");
            uiElements.addLogToLogArea("Account " + account.getFullName() + " is not active");
            return;
        }

        if (account.getWorkInShabat() != null && !account.getWorkInShabat() && BotinokUtils.checkShabatDay()) {
            log.info("Shabat day, skip user " + account.getFullName());
            uiElements.addLogToLogArea("Shabat day. Skip");
            return;
        }

        linkedinBotService.start(account);

        linkedinBotService.checkAuthorizationAndLogin();
        if (linkedinBotService.parseUserName()) {
            accountList = clientAccountService.findAllActive();
            uiElements.updateAccounts(accountList, account.getId());
        }

        uiElements.addLogToLogArea("Start bot for user " + account.getFullName());

        linkedinBotService.applyToPositions();

        linkedinBotService.searchConnectsAndConnect();

        if ( ! linkedinBotService.parseLinkedinUser()){
            linkedinBotService.parseLinkedinUrlOfConnections();
            linkedinBotService.parseLinkedinUser();
        }

        linkedinBotService.close();
    }
}
