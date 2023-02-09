package digital.moveto.botinok.client.linkedin;

import com.microsoft.playwright.Playwright;
import digital.moveto.botinok.client.config.ClientConst;
import digital.moveto.botinok.client.config.GlobalConfig;
import digital.moveto.botinok.client.feign.AccountFeignClient;
import digital.moveto.botinok.client.service.ClientAccountService;
import digital.moveto.botinok.client.ui.MainScene;
import digital.moveto.botinok.client.ui.UiElements;
import digital.moveto.botinok.client.utils.FileUtils;
import digital.moveto.botinok.model.entities.Account;
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
    private AccountFeignClient accountFeignClient;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void runInThread(Runnable runnable) {
        singleThreadExecutor.submit(runnable);
    }

    @PostConstruct
    public void init() {
        addAllAccountToUi();
        mainScene.finishInitialization();

        Playwright.create().close();

        uiElements.getStartButton().setOnMouseClicked(event -> {
            if (uiElements.getStartButton().getText().equals("Start")) {
                uiElements.changeButtonState(false);

                runInThread(() -> {
                    try {
                        log.info("Click button start bot");
                        if (!uiElements.saveSettingForUser()) {
                            return; // have error in save settings
                        }
                        startSearchConnectsAndConnect();
                    } catch (Exception e) {
                        if (e.getCause().getClass().equals(InterruptedException.class)){
                            uiElements.addLogToLogArea("Bot stopped");
                        } else {
                            log.error("Error in startSearchConnectsAndConnect", e);
                            uiElements.addLogToLogArea("Error in Application. Please, restart application");
                        }
                    } finally {
                        uiElements.changeButtonState(true);
                    }
                });
            } else {
                log.info("Click button stop bot");
                uiElements.addLogToLogArea("Stop bot");
                uiElements.changeButtonState(true);
                singleThreadExecutor.shutdownNow();
                singleThreadExecutor = Executors.newSingleThreadExecutor();
            }
        });



        uiElements.addLogToLogArea("Loading complete");
        uiElements.changeButtonState(true);
        uiElements.getStartButton().setCursor(Cursor.HAND);

    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 10)
    public void startBotByScheduled() {

        if (globalConfig.startByDefault) {

            runInThread(() -> {
                try {
                    startSearchConnectsAndConnect();
                } catch (Exception e) {
                    if (e.getCause().getClass().equals(InterruptedException.class)){
                        uiElements.addLogToLogArea("Bot stopped");
                    } else {
                        log.error("Error in startSearchConnectsAndConnect", e);
                        uiElements.addLogToLogArea("Error in Application. Please, restart application");
                    }
                } finally {
                    uiElements.changeButtonState(true);
                }
            });
        }
    }

    private void addAllAccountToUi(){
        List<Account> accounts = clientAccountService.findAll();
        if (accounts == null || accounts.isEmpty()) {
            accounts.add(clientAccountService.addNewAccount());
        }
        uiElements.updateAccounts(accounts);
    }

    public void startSearchConnectsAndConnect() {
        uiElements.addLogToLogArea("Initialization bot");
        uiElements.changeButtonState(false);
        log.info("Start LinkedinBotStarter");

        FileUtils.mkdirs(globalConfig.pathToStateFolder);   //if we don't have a folder, we create it

        List<Account> allActiveAccounts = clientAccountService.findAll();

        for (int i = 0; i < allActiveAccounts.size(); i++) {
            Account account = clientAccountService.findById(allActiveAccounts.get(i).getId()).get();

            log.info("Start bot for user " + account.getFirstName());

            try (LinkedinBotService linkedinBotService = context.getBean(LinkedinBotService.class)) {

                botWork(linkedinBotService, account);

                if (i != allActiveAccounts.size() - 1) {
                    log.info("Timeout between users " + ClientConst.SLEEP_BETWEEN_START_BOT_FOR_DIFFERENT_USERS);
                    linkedinBotService.sleepRandom(ClientConst.SLEEP_BETWEEN_START_BOT_FOR_DIFFERENT_USERS);
                }
            } catch (Exception e) {
                log.error("Error bot for user " + account.getFullName() + ", UUID = " + account.getId(), e);

                if (e.getCause().getClass().equals(InterruptedException.class)){
                    throw e;
                }
            }
        }

        log.info("End startSearchConnectsAndConnect");
        uiElements.changeButtonState(true);
        uiElements.addLogToLogArea("Successfully finished");
    }

    private void botWork(LinkedinBotService linkedinBotService, Account account) {

        List<Account> accountList = clientAccountService.findAll();
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

        if (account.getActiveSearch()) {
            linkedinBotService.applyToPositions();
        }

        linkedinBotService.searchConnectsAndConnect();

        if (LocalDate.now().getDayOfMonth() == 20
                || account.getContacts() == null
                || account.getContacts().isEmpty()) {
            linkedinBotService.parseLinkedinUrlOfConnections();
        }

        linkedinBotService.parseLinkedinUser();

        linkedinBotService.close();
    }
}
