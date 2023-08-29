package digital.moveto.botinok.client.linkedin;

import digital.moveto.botinok.client.config.GlobalConfig;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.client.service.ClientAccountService;
import digital.moveto.botinok.client.ui.MainScene;
import digital.moveto.botinok.client.utils.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static digital.moveto.botinok.client.config.ClientConst.DEFAULT_FOLDER_FOR_DEFAULT_USER_DATA_DIR;
import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestPropertySource("classpath:test.properties")
public class LinkedinBotServiceTest {
//
//    private final String loginTestUser = "Etopochta0007@gmail.com";
//    private final String loginTestPassword = "hW36LJGGm9AHqC9";
//
//    @Autowired
//    private ApplicationContext context;
//
//    @Autowired
//    private GlobalConfig globalConfig;
//
//    @Autowired
//    private ClientAccountService clientAccountService;
//
//    @Autowired
//    private MainScene mainScene;
//
//    private LinkedinBotService linkedinBotService;
//
//    @BeforeEach
//    public void init() {
//        this.linkedinBotService = context.getBean(LinkedinBotService.class);
//    }
//
//    @AfterEach
//    public void close() {
//        linkedinBotService.close();
//        clientAccountService.deleteByFolder("temp");
//        FileUtils.deleteFolder(globalConfig.pathToStateFolder + "temp");
//    }
//
//    @Test
//    public void checkAuthorizationNotAuthorize() {
//        Account account = clientAccountService.findOrCreateAccountWithContacts("not_authorize");
//        linkedinBotService.start(account);
//        boolean checkAuthorization = linkedinBotService.checkAuthorization();
//        assertThat(checkAuthorization).isEqualTo(false);
//    }
//
//    @Test
//    public void checkAuthorizationIsAuthorize() {
//        Account account = clientAccountService.findOrCreateAccountWithContacts(DEFAULT_FOLDER_FOR_DEFAULT_USER_DATA_DIR);
//        linkedinBotService.start(account);
//        boolean checkAuthorization = linkedinBotService.checkAuthorization();
//        assertThat(checkAuthorization).isEqualTo(true);
//    }
//
//    @Test
//    public void checkAuthorizationAndSaveLoginAndPass() {
//        Account account = clientAccountService.findOrCreateAccountWithContacts("default");
//        linkedinBotService.start(account);
//
//        boolean checkAuthorization = linkedinBotService.checkAuthorization();
//        assertThat(checkAuthorization).isEqualTo(false);
//        assertThat(account.getLogin()).isNull();
//        assertThat(account.getPassword()).isNull();
//
//        linkedinBotService.getPlaywrightService().getElementByLocator("input[name=session_key]").ifPresent(e -> e.type(loginTestUser));
//        linkedinBotService.getPlaywrightService().getElementByLocator("input[name=session_password]").ifPresent(e -> e.type(loginTestPassword));
//
//        new Thread(() -> {
//            linkedinBotService.sleep(2000,false);
//            linkedinBotService.getPlaywrightService().click("button[type=submit]");
//
//        }).start();
//
//        linkedinBotService.login();
//        linkedinBotService.sleep(2000, false);
//
//        account = clientAccountService.findOrCreateAccountWithContacts("temp");
//
//        assertThat(account.getLogin()).isEqualTo(loginTestUser);
//        assertThat(account.getPassword()).isEqualTo(loginTestPassword);
//        checkAuthorization = linkedinBotService.checkAuthorization();
//        assertThat(checkAuthorization).isEqualTo(true);
//    }
//
//
//    @Test
//    public void checkAuthorizationWithLoginAndPassInDb() {
//        Account account = clientAccountService.findOrCreateAccountWithContacts("temp");
//        account.setLogin(loginTestUser);
//        account.setPassword(loginTestPassword);
//        linkedinBotService.start(account);
//
//        boolean checkAuthorization = linkedinBotService.checkAuthorization();
//        assertThat(checkAuthorization).isEqualTo(false);
//
//        linkedinBotService.login(account.getLogin(), account.getPassword());
//        linkedinBotService.sleep(2000, false);
//
//        checkAuthorization = linkedinBotService.checkAuthorization();
//        assertThat(checkAuthorization).isEqualTo(true);
//    }


}
