package digital.moveto.botinok.client.linkedin;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.PlaywrightException;
import digital.moveto.botinok.client.config.ClientConst;
import digital.moveto.botinok.client.config.GlobalConfig;
import digital.moveto.botinok.client.exeptions.StopMadeContactException;
import digital.moveto.botinok.client.playwright.PlaywrightService;
import digital.moveto.botinok.client.service.*;
import digital.moveto.botinok.client.ui.UiElements;
import digital.moveto.botinok.client.utils.UrlUtils;
import digital.moveto.botinok.model.entities.*;
import digital.moveto.botinok.model.entities.enums.LocationProperty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static digital.moveto.botinok.client.config.ClientConst.*;
import static java.lang.Character.MAX_RADIX;

@Service
@RequiredArgsConstructor
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkedinBotService implements AutoCloseable {
    Logger log = LoggerFactory.getLogger(LinkedinBotService.class);

    @Autowired
    private PlaywrightService playwrightService;

    @Autowired
    private ClientAccountService clientAccountService;

    @Autowired
    private ClientMadeContactService clientMadeContactService;

    @Autowired
    private ClientMadeApplyService clientMadeApplyService;

    @Autowired
    private ClientContactService clientContactService;

    @Autowired
    private ClientCompanyService clientCompanyService;

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private UiElements uiElements;

    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public PlaywrightService getPlaywrightService() {
        return playwrightService;
    }

    @Transactional
    public void start(Account account) {
        log.info("Start LinkedinBotService for user " + account.getFullName());
        this.account = account;

        //check license
        if (account.getEndDateLicense() != null && account.getEndDateLicense().isBefore(LocalDate.now())) {
            log.error("Linkedin account {} is expired", account.getFullName());
            throw new RuntimeException("Linkedin account " + account.getFullName() + " is expired.");
        }

        playwrightService.start(Paths.get(globalConfig.pathToStateFolder + account.getFolder()), globalConfig.headlessBrowser);
    }

    private void closeMsgDialogs() {
        Collection<ElementHandle> closeConversations = playwrightService.getElementsWithText("Close your conversation with ");
        closeConversations.forEach(elementHandle -> {
            elementHandle.click();
            playwrightService.sleepRandom(1000);
        });
    }

    public void searchConnectsAndConnect() {
        AtomicInteger count = new AtomicInteger(clientMadeContactService.getCountOfTodayForAccount(account));

        if (count.get() >= account.getCountDailyConnect()){
            return;
        }

        log.info("Start search connects for user " + account.getFullName());

        goSearchRandomKeywordsAndPageAndSid();

        CREATE_CONNECTS:
        for (int page = 0; page < MAX_COUNT_PAGE_FOR_ONE_TIME; page++) {
            playwrightService.sleepRandom(3000);
            closeMsgDialogs();
            log.info("Start search connects for page " + page + "/" + MAX_COUNT_PAGE_FOR_ONE_TIME + ", user " + account.getFullName());
            Collection<ElementHandle> connectButtons = playwrightService.getElementsWithCurrentText("Connect");
            for (ElementHandle elementHandle : connectButtons) {
                try {
                    madeContact(elementHandle, count);
                } catch (StopMadeContactException e) {
                    break CREATE_CONNECTS;
                } catch (Exception e) {
                    log.error("Error made contact", e);
                    break CREATE_CONNECTS;
                }
            }
            goSearchRandomKeywordsAndPageAndSid();
        }

        log.info("We made " + count.get() + " connections for user " + account.getFullName());

    }

    private void clickButtonNext() {
        for (int i = 0; i < 10; i++) {
            Optional<ElementHandle> nextBtn = playwrightService.getElementWithCurrentText("Next");
            if (nextBtn.isPresent()) {
                log.debug("Click next button");
                nextBtn.get().click();
                break;
            }
            log.warn("Next button not found. Reload page " + i + "/10");

            playwrightService.reload();
            playwrightService.sleepRandom(5000);
        }
    }

    private void goNextPage() {
        try {
            Map<String, String> stringListMap = UrlUtils.splitQuery(playwrightService.getCurrentUrl());
            int nextPage = (Integer.parseInt(stringListMap.get("page")) + 1);
            if (nextPage > ClientConst.MAX_PAGE_ON_LINKEDIN) {
                nextPage = 1;
            }
            stringListMap.put("page", String.valueOf(nextPage));
            String nextPageUrl = playwrightService.getCurrentUrl().split("\\?")[0] + "?" + UrlUtils.createQueryString(stringListMap);
            playwrightService.open(nextPageUrl);
        } catch (Exception e) {
            clickButtonNext();
        }
    }

    private void goRandomPage() {
        try {
            Map<String, String> stringListMap = UrlUtils.splitQuery(playwrightService.getCurrentUrl());
            int nextPage = (int) (Math.random() * 100) + 1;
            stringListMap.put("page", String.valueOf(nextPage));
            String nextPageUrl = playwrightService.getCurrentUrl().split("\\?")[0] + "?" + UrlUtils.createQueryString(stringListMap);
            playwrightService.open(nextPageUrl);
        } catch (Exception e) {
            clickButtonNext();
        }
    }

    private void goSearchRandomKeywordsAndPageAndSid() {
        try {
            Map<String, String> stringListMap = UrlUtils.splitQuery(ClientConst.DEFAULT_URL_FOR_SEARCH);

            int nextPage = (int) (Math.random() * 100) + 1;
            stringListMap.put("page", String.valueOf(nextPage));
            stringListMap.put("keywords", getRandomSearchKeyword());
            stringListMap.put("sid", generateRandomSid());
            if (account.getLocation() != null){
                stringListMap.put("geoUrn", "%5B%22" + LocationProperty.getByKey(account.getLocation()).getLinkedinId() + "%22%5D");
            }

            String nextPageUrl = ClientConst.DEFAULT_URL_FOR_SEARCH_WITHOUT_PARAMS + UrlUtils.createQueryString(stringListMap);

            log.debug("Go to random page and random keywords " + nextPageUrl);
            playwrightService.open(nextPageUrl);
        } catch (Exception e) {
            clickButtonNext();
        }
    }

    private String generateRandomSid() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            result.append(Integer.toString((int) (Math.random() * MAX_RADIX), MAX_RADIX));
        }
        return result.toString();
    }

    private String getRandomSearchKeyword() {
        int id = (int) (Math.random() * ClientConst.SEARCH_KEYWORDS.size());
        return ClientConst.SEARCH_KEYWORDS.get(id);
    }

    private void madeContact(ElementHandle elementHandle, AtomicInteger count) {

        if (count.get() >= account.getCountDailyConnect()) {
            log.info("Stop create connects for user " + account.getFullName());
            throw new StopMadeContactException("Stop create connects for user " + account.getFullName());
        }
        elementHandle.click();  // click connect button
        playwrightService.sleepRandom(500);

        // if we have a limit of connections
        if (playwrightService.isTextFind("invitation limit")) {
            log.info("We have a limit of connections for user " + account.getFullName());
            throw new StopMadeContactException("We have a limit of connections for user " + account.getFullName());
        }

        Optional<ElementHandle> howTheyKnowYou = playwrightService.getElementWithCurrentText("This helps them remember how they know you.");
        Optional<ElementHandle> other = playwrightService.getElementWithCurrentText("Other");
        if (other.isPresent() && howTheyKnowYou.isPresent()) {
            other.ifPresent(ElementHandle::click);
            playwrightService.sleepRandom(500);
            playwrightService.getElementWithCurrentText("Connect").ifPresent(ElementHandle::click);
            playwrightService.sleepRandom(500);
        }

        String contactName = "";
        Optional<ElementHandle> elementWithContactName = playwrightService.getByText("You can add a note to personalize your invitation to ");
        if (elementWithContactName.isPresent()) {
            String nameWithDot = elementWithContactName.get().innerText().trim().replace("You can add a note to personalize your invitation to ", "");
            contactName = nameWithDot.substring(0, nameWithDot.length() - 1);
        }

        playwrightService.getElementWithCurrentText("Add a note").ifPresent(ElementHandle::click);
        playwrightService.sleepRandom(500);

        final String inviteMessage = generateInviteMessage();
        playwrightService.getElementByLocator("textarea[name=message]").ifPresent(eh -> eh.type(inviteMessage, new ElementHandle.TypeOptions().setDelay(10)));
        playwrightService.sleepRandom(2000);
        playwrightService.getElementWithCurrentText("Send").ifPresent(ElementHandle::click);
        playwrightService.sleepRandom(500);

        // if we have a limit of connections
        if (playwrightService.isTextFind("invitation limit")) {
            log.info("We have a limit of connections for user " + account.getFullName());
            throw new StopMadeContactException("We have a limit of connections for user " + account.getFullName());
        }

        MadeContact madeContact = MadeContact.builder()
                .date(LocalDateTime.now())
                .name(contactName)
                .account(account).build();
        clientMadeContactService.save(madeContact);
        uiElements.updateStatistic();

        log.info("Connect #" + count.incrementAndGet() + ", with '" + contactName + "' for user " + account.getFullName());

        uiElements.addLogToLogArea("Connect with: " + contactName);

    }

    private String generateInviteMessage() {
        for (int i = 0; i < 1_000; i++) {
            String result = randomString("Hi", "Hi there", "Hello", "Good day", "Greetings", "Hey", "Hey there", "Good afternoon")
                    + randomString(",", "!", ".")
                    + randomString(" ", "\n");

            if (Strings.isNotBlank(account.getFirstName()) && Strings.isNotBlank(account.getLastName())) {
                result += randomString("",
                        "My name is " + account.getFullName() + "." + randomString(" ", "\n"),
                        "My name is " + account.getLastName() + " " + account.getFirstName() + "." + randomString(" ", "\n"),
                        "My name is " + account.getFirstName() + "." + randomString(" ", "\n"));
            }

            if (Strings.isNotBlank(account.getPosition())) {
                result += randomString("",
                        "I'm " + randomString(account.getPosition()) + "." + randomString(" ", "\n"));
            }

            if (account.getActiveSearch() != null && account.getActiveSearch()) {
                result += randomString("",
                        "I'm looking for a job." + randomString(" ", "\n"),
                        "I'm looking for a job in IT." + randomString(" ", "\n"),
                        "I'm searching a different opportunities." + randomString(" ", "\n"),
                        "I'm looking for a new job." + randomString(" ", "\n"),
                        "I'm looking for a new job in IT." + randomString(" ", "\n"),
                        "Do you have some available opportunities for me?" + randomString(" ", "\n"));
            }

            result += randomString("I'm interested in your company and I would like to connect with you", "I would love to join your network", "I would like to join your network", "I'd like to add you to my professional network on LinkedIn")
                    + randomString("", " :)")
                    + randomString("", "!", ".")
                    + randomString("\n\n", "\n", "\n\n", "\n")
                    + randomString("Wish you a great day", "Best regards", "Best wishes", "Kind regards", "Regards", "Sincerely", "Warm regards")
                    + randomString("", "!", ".");

            if (result.length() < 300) {
                return result;
            }
        }

        String miniMessage = "Hi, I'd like to add you to my professional network on LinkedIn.";
        log.info("We can't generate message for user " + account.getFullName() + ", use mini message: " + miniMessage);
        return miniMessage;
    }

    private String randomString(String... strings) {
        List<String> collectionSuggest = takeListString(strings);
        String result = collectionSuggest.get((int) (Math.random() * collectionSuggest.size()));
        return trimIfNeed(result);
    }

    private List<String> takeListString(String... strings) {
        List<String> collectionSuggest = new ArrayList<>(strings.length);
        for (String string : strings) {
            String[] split = string.split(ClientConst.SPLIT_FOR_RANDOM_KEYWORDS);
            collectionSuggest.addAll(split.length > 1 ? Arrays.asList(split) : Collections.singletonList(string));
        }
        return collectionSuggest;
    }

    private String takeString(int number, String... strings) {
        List<String> collectionSuggest = takeListString(strings);
        number = number % collectionSuggest.size();
        String result = trimIfNeed(collectionSuggest.get(number));
        return result;
    }

    private String trimIfNeed(String text) {
        if (text.length() > 3){
//            text = text.trim();
        }
        return text;
    }

    void login(String login, String password) {
        openNotHeadlessBrowserIfNeed();
        openLinkedInPageIfNeed();

        playwrightService.type("input[name=session_key]", login);
        playwrightService.type("input[name=session_password]", password);
        playwrightService.click("button[type=submit]");
        sleepRandom(500);
        playwrightService.waitForSelector("input[placeholder=Search]");

        closeHeadlessBrowserIfNeed();
    }

    private void openLinkedInPageIfNeed() {
        if (playwrightService.getCurrentUrl() != null && !playwrightService.getCurrentUrl().contains("linkedin.com")) {
            playwrightService.open(ClientConst.LINKEDIN_URL);
            playwrightService.sleep(5000, false);
        }
    }

    private void openNotHeadlessBrowserIfNeed() {
        if (globalConfig.headlessBrowser) {
            playwrightService.close();
            playwrightService.start(Paths.get(globalConfig.pathToStateFolder + account.getFolder()), false);
        }
    }

    private void closeHeadlessBrowserIfNeed() {
        if (!globalConfig.headlessBrowser) {
            playwrightService.close();
            playwrightService.start(Paths.get(globalConfig.pathToStateFolder + account.getFolder()), true);
        }
    }

    void login() {
        try {
            uiElements.addLogToLogArea("Please authorize. You have 20 minutes for that.");
            openNotHeadlessBrowserIfNeed();
            openLinkedInPageIfNeed();

            log.info("Please authorize for user " + account.getFullName() + ". You have 20 minutes for that.");

            AtomicReference<String> login = new AtomicReference<>();
            AtomicReference<String> password = new AtomicReference<>();
            for (int i = 0; i < 5_000_000; i++) {
                playwrightService.getElementByLocator("input[name=session_key]").ifPresent(e -> login.set(e.inputValue()));
                playwrightService.getElementByLocator("input[name=session_password]").ifPresent(e -> password.set(e.inputValue()));
                account.setLogin(login.get());
                account.setPassword(password.get());

                if (checkAuthorization()) {
                    log.info("Authorization success for user " + account.getLogin());
                    uiElements.addLogToLogArea("Authorization success for user " + account.getLogin());
                    sleep(2000, false);
                    return;
                }
                playwrightService.sleep(1);
            }
        } catch (PlaywrightException e) {
            // nothing to do.
        } finally {
            account = clientAccountService.save(account);
            closeHeadlessBrowserIfNeed();
        }
    }

    private void search(String query) {
        playwrightService.type("input[placeholder=Search]", query);
        playwrightService.click("button[type=submit]");
    }

    public void checkAuthorizationAndLogin() {
        if ( !checkAuthorization()) {
            if (Strings.isNotBlank(account.getLogin()) && Strings.isNotBlank(account.getPassword())) {
                login(account.getLogin(), account.getPassword());
            } else {
                login();
            }
        }
    }

    /**
     * @return true if authorized
     */
    boolean checkAuthorization() {
        openLinkedInPageIfNeed();
        return playwrightService.isSelectorFind("input[placeholder=Search]");
    }

    @Override
    public void close() {
        if (playwrightService != null) {
            playwrightService.close();
        }
    }

    public void sleepRandom(int timeout) {
        playwrightService.sleepRandom(timeout);
    }

    public void sleep(int timeout) {
        sleep(timeout, true);
    }
    public void sleep(int timeout, boolean changeSpeedOfBot) {
        playwrightService.sleep(timeout, changeSpeedOfBot);
    }

    public boolean parseUserName(){
        if (account.getFirstName() == null || account.getLastName() == null
        || account.getFirstName().isEmpty() || account.getLastName().isEmpty()) {
            if ( ! "https://www.linkedin.com/feed/".equalsIgnoreCase(playwrightService.getCurrentUrl())) {
                playwrightService.open("https://www.linkedin.com/feed/");
                sleep(3000);
            }
            Optional<ElementHandle> elementByLocator = playwrightService.getElementByLocator("div > div > div > a > div.t-16.t-black.t-bold");
            if (elementByLocator.isPresent()) {
                String text = elementByLocator.get().textContent();
                log.info("User name: " + text);
                if (text != null && text.contains(" ")) {
                    text = text.trim();
                    account.setFirstName(text.substring(0, text.indexOf(" ")));
                    account.setLastName(text.substring(text.indexOf(" ") + 1));
                    clientAccountService.save(account);
                    return true;
                }
            } else {
                log.error("Can't find user name");
            }
        }
        return false;
    }


    public void parseLinkedinUrlOfConnections() {
        log.info("Start parseLinkedinUrlOfConnections for user " + account.getFullName());
        playwrightService.open(ClientConst.DEFAULT_URL_FOR_MY_CONNECTIONS);
        playwrightService.sleepRandom(3000);

        for (int i = 0; i < 500; i++) {
            Optional<ElementHandle> showMoreResults = playwrightService.getElementByLocator("text=Show more results");
            if (showMoreResults.isPresent()) {
                try {
                    parseLinkedinUrlOfConnectionsScrollDownAndOpenAllUsers();
                } catch (Exception e) {
                    log.error("Error while parseLinkedinUrlOfConnectionsScrollDownAndOpenAllUsers", e);
                }
                parseLinkedinUrlOfConnectionsStartParseLinkAndMainInformation();
                playwrightService.sleepRandom(10000);
            } else {
                break;
            }
        }

    }

    private void parseLinkedinUrlOfConnectionsScrollDownAndOpenAllUsers() {
        for (int i = 0; i < 50; i++) {
//            playwrightService.scrollToSelector("text=Show more results");
            Optional<ElementHandle> showMoreResults = playwrightService.getElementByLocator("text=Show more results");
            if (showMoreResults.isPresent()) {
                log.trace("Scroll down for user " + account.getFullName() + " and open all users. Step " + i);
                showMoreResults.get().click();
                playwrightService.sleepRandom(5000);
            } else {
                break;
            }
        }
    }

    private void parseLinkedinUrlOfConnectionsStartParseLinkAndMainInformation(){
        List<ElementHandle> linkedinUsers = playwrightService.getElementsByLocator("div.mn-connection-card__details");

        for (int i = 0; i < linkedinUsers.size(); i++) {
            ElementHandle linkedinUserElementHandle = linkedinUsers.get(i);
            ElementHandle elementHandleA = linkedinUserElementHandle.querySelector("a");
            String linkedinUrl = elementHandleA.getAttribute("href");

            if (linkedinUrl.startsWith("/")) {
                linkedinUrl = "https://www.linkedin.com" + linkedinUrl;
            }

            Optional<Contact> userInDbByLinkedinUrl = clientContactService.findByLinkedinUrl(linkedinUrl);
            if (userInDbByLinkedinUrl.isPresent()) {
                log.debug("User " + linkedinUrl + " already in db");
                continue;
            }

            Contact contact = new Contact();
            contact.setAccount(account);
            contact.setLinkedinUrl(linkedinUrl);

            ElementHandle LinkedinUserName = elementHandleA.querySelector("span.t-16.t-black.t-bold");

            if (LinkedinUserName != null) {
                String name = LinkedinUserName.textContent();
                if (name != null && name.contains(" ")) {
                    name = name.trim();
                    contact.setFirstName(name.substring(0, name.indexOf(" ")));
                    contact.setLastName(name.substring(name.indexOf(" ") + 1));
                }
            }
            ElementHandle LinkedinUserPosition = elementHandleA.querySelector("span.t-14.t-black--light.t-normal");
            if (LinkedinUserPosition != null) {
                contact.setPosition(LinkedinUserPosition.textContent().trim());
            }

            clientContactService.save(contact);
            log.debug("User " + linkedinUrl + " saved.");

        }
    }

    public boolean parseLinkedinUser() {
        log.info("Start parseLinkedinUser for user " + account.getFullName());
        this.account = clientAccountService.findById(account.getId()).get();
        List<Contact> contacts = account.getContacts();
        if (contacts == null || contacts.isEmpty()) {
            log.info("No users for parse");
            return false;
        }

        Collections.shuffle(contacts);

        int count = clientContactService.getCountOfParseTodayForAccount(account);
        for (int i = 0; i < contacts.size() && count < globalConfig.countParseForOneTime; i++) {
            Contact contact = contacts.get(i);

            if (contact.getParseDate() != null) {
                continue;
            }

            log.debug("Parse user " + i + "/" + contacts.size() + ". " + contact.getLinkedinUrl());
            playwrightService.open(contact.getLinkedinUrl() + "/overlay/contact-info/");
            playwrightService.sleepRandom(5000);

            // Phone
            Optional<ElementHandle> elementByLocator =
                    playwrightService.getElementByLocator("section > div > section.pv-contact-info__contact-type.ci-phone > ul > li > span.t-14.t-black.t-normal");
            if (elementByLocator.isPresent()) {
                String text = elementByLocator.get().textContent().trim();
                contact.setPhone(text);
            }

            // Email
            elementByLocator =
                    playwrightService.getElementByLocator("section > div > section.pv-contact-info__contact-type.ci-email > div > a");
            if (elementByLocator.isPresent()) {
                String text = elementByLocator.get().textContent().trim();
                contact.setEmail(text);
            }

            // Location
            elementByLocator =
                    playwrightService.getElementByLocator("section > div > section.pv-contact-info__contact-type.ci-address > div > a");
            if (elementByLocator.isPresent()) {
                String text = elementByLocator.get().textContent().trim();
                if (text.length() < 200) {
                    contact.setLocation(text);
                }
            }
            if (contact.getLocation() == null) {
                playwrightService.click(10, 10);
                playwrightService.sleepRandom(300);
                elementByLocator =
                        playwrightService.getElementByLocator("div.ph5.pb5 > div.mt2.relative > div.pv-text-details__left-panel.mt2 > span.text-body-small.inline.t-black--light.break-words");
                if (elementByLocator.isPresent()) {
                    String text = elementByLocator.get().textContent().trim();
                    contact.setLocation(text);
                }
            }

//            String html = playwrightService.getPage().innerHTML("html");
//            linkedinUser.setHtml(html);

            contact.setUpdatedDate(LocalDate.now());
            contact.setParseDate(LocalDate.now());
            clientContactService.saveAndFlush(contact);
            log.debug("User " + ++count + "/" + contacts.size() + ". Parsed and save.");
            log.debug(contact.toString());

        }
        if (count < globalConfig.countParseForOneTime) {
            return false;
        } else {
            return true;
        }
    }

    public void applyToPositions(){
        AtomicInteger countApply = new AtomicInteger(clientMadeApplyService.getCountOfTodayForAccount(getAccount()));
        if (countApply.get() >= account.getCountDailyApply()){
            return;
        }

        uiElements.addLogToLogArea("Start apply to positions");
        log.info("Start apply to positions for user: " + account.getFullName());
//        https://www.linkedin.com/jobs/search/?currentJobId=3450022774&f_AL=true&f_TPR=r86400&geoId=101620260&keywords=java%20developer&location=Israel&refresh=true
//        https://www.linkedin.com/jobs/search/?currentJobId=3462346515&f_AL=true&f_TPR=r86400&geoId=101620260&keywords=java%20developer&location=Israel&refresh=true&start=25

        int initPosition = (int) (Math.random() * 100000);
        int countPositionsForCurrentAccount = takeListString(account.getPosition()).size();
        for (int i = 0; i < countPositionsForCurrentAccount; i++) {
            String position = takeString(initPosition + i, account.getPosition()).trim();

            for (int j = 0; j < COUNT_PAGE_FOR_SEARCH_POSITIONS; j++) {
                if (countApply.get() > account.getCountDailyApply()) {
                    return;
                }
                int start = j * COUNT_POSITION_ON_ONE_PAGE;
                playwrightService.open("https://www.linkedin.com/jobs/search/?f_AL=true&f_TPR=r86400&geoId=" + LocationProperty.getLocation(account.getLocation()).getLinkedinId() + "&keywords=" + position + "&location=" + LocationProperty.getLocation(account.getLocation()).getName() + "&refresh=true&start=" + start);
                playwrightService.sleepRandom(3000);
                if (playwrightService.getByText("No matching jobs found.").isPresent()) {
                    log.info("No matching jobs found.");
                    break;
                }

                applyToPositionsOnCurrentPage(countApply);
            }
        }
        log.info("Finish apply to positions for user: " + account.getFullName());
        uiElements.addLogToLogArea("Finish apply to positions");
    }

    public void applyToPositionsOnCurrentPage(AtomicInteger countApply) {
        // Scroll to bottom for show all positions
        for (int i = 0; i < 5; i++) {
            playwrightService.scrollMouseDown(300, 500, 1000);
            playwrightService.sleepRandom(2000);
        }
        List<ElementHandle> easyApply = playwrightService.getElementsByLocator("div.job-card-container");
        log.debug("Find a " + easyApply.size() + " positions for user " + account.getFullName());

        for (int i = 0; i < easyApply.size() && countApply.get() < account.getCountDailyApply(); i++) {
            easyApply.get(i).click();
            playwrightService.sleepRandom(3000);

            applyToCurrentPosition(countApply);
        }
    }

    private void applyToCurrentPosition(AtomicInteger countApply){
        MadeApply madeApply = new MadeApply();
        madeApply.setAccount(account);

        Optional<ElementHandle> positionNameElement = playwrightService.getElementByLocator("div.jobs-details > div div.jobs-unified-top-card > div div > a > h2");
        if (positionNameElement.isPresent()){
            madeApply.setPosition(positionNameElement.get().textContent().trim());
            String linkToPosition = playwrightService.getElementByLocator("div.jobs-details > div div.jobs-unified-top-card > div div > a").get().getAttribute("href");
            linkToPosition = linkToPosition.substring(0, linkToPosition.indexOf("?"));
            if (linkToPosition.startsWith("/")) {
                linkToPosition = ClientConst.LINKEDIN_URL + linkToPosition;
            }
            madeApply.setLink(linkToPosition);
        }

        Optional<ElementHandle> companyNameElement = playwrightService.getElementByLocator("div.jobs-details > div div.jobs-unified-top-card > div div > div > span > span > a");
        if (companyNameElement.isPresent()){
            String name = companyNameElement.get().textContent().trim();
            String linkToCompany = companyNameElement.get().getAttribute("href");
            int endIndex = linkToCompany.indexOf("?");
            if (endIndex > 0) {
                linkToCompany = linkToCompany.substring(0, endIndex);
            }
            if (linkToCompany.startsWith("/")) {
                linkToCompany = ClientConst.LINKEDIN_URL + linkToCompany;
            }
            Optional<Company> companyInDb = clientCompanyService.findByLink(linkToCompany);
            if (companyInDb.isPresent()){
                madeApply.setCompany(companyInDb.get());
            } else {
                Company company = new Company();
                company.setName(name);
                company.setLink(linkToCompany);
                company = clientCompanyService.saveAndFlush(company);
                madeApply.setCompany(company);
            }
        } else {
            Optional<Company> companyInDb = clientCompanyService.findByLink(null);
            if (companyInDb.isPresent()){
                madeApply.setCompany(companyInDb.get());
            } else {
                Company company = new Company();
                company.setName(null);
                company.setLink(null);
                company = clientCompanyService.saveAndFlush(company);
                madeApply.setCompany(company);
            }
        }

//            Optional<ElementHandle> easyApplyBtn = playwrightService.getElementWithCurrentText("Easy Apply");

        Optional<ElementHandle> elementByLocator = playwrightService.getElementByLocator("button.jobs-apply-button > span");
        if (elementByLocator.isPresent()) {

            String suggestEasyApplyBtn = elementByLocator.get().textContent().trim();
            if (suggestEasyApplyBtn.equals("Easy Apply")) {
                elementByLocator.get().click();
                playwrightService.sleepRandom(1000);
                for (int j = 0; j < 10; j++) {

                    Optional<ElementHandle> uploadResume = playwrightService.getElementWithCurrentText("Be sure to include an updated resume");
                    if (uploadResume.isPresent()){
                        playwrightService.getElementWithCurrentText("Choose").ifPresent(ElementHandle::click);
                        playwrightService.sleepRandom(1000);
                    }

                    Optional<ElementHandle> nextBtn = playwrightService.getElementByLocator("button[aria-label=\"Continue to next step\"] > span");
                    if (nextBtn.isPresent() && nextBtn.get().innerText().equals("Next")) {
                        nextBtn.get().click();
                        playwrightService.sleepRandom(1000);
                    }

                    Optional<ElementHandle> reviewBtn = playwrightService.getElementByLocator("button[aria-label=\"Review your application\"] > span");
                    if (reviewBtn.isPresent() && reviewBtn.get().innerText().equals("Review")){
                        reviewBtn.get().click();
                        playwrightService.sleepRandom(1000);
                    }

                    Optional<ElementHandle> submitBtn = playwrightService.getElementWithCurrentText("Submit application");
                    if (submitBtn.isPresent()){
                        submitBtn.get().click();

                        log.info("Submit application #" + countApply.incrementAndGet() + " for position " + madeApply.getPosition() + " in company " + madeApply.getCompany().getName());
                        madeApply.setDate(LocalDateTime.now());
                        madeApply = clientMadeApplyService.save(madeApply);
                        uiElements.updateStatistic();
                        uiElements.addLogToLogAreaWithLinks("Submit application for position " + madeApply.getPosition() + " ($) in company " + madeApply.getCompany().getName() + " ($)", Arrays.asList(madeApply.getLink(), madeApply.getCompany().getLink()));

                        playwrightService.sleepRandom(3000);
                        break;
                    }
                }

                playwrightService.click(10, 10);
                playwrightService.sleepRandom(200);
                Optional<ElementHandle> discardBtn = playwrightService.getElementByLocator("button[data-control-name=discard_application_confirm_btn] > span");
                if (discardBtn.isPresent() && discardBtn.get().innerText().equals("Discard")){
                    discardBtn.get().click();
                    log.debug("Discard application #" + countApply.get() + " for position " + madeApply.getPosition() + " in company " + madeApply.getCompany().getName());
                    playwrightService.sleepRandom(1000);
                }

            }
        }
    }

//    @Transactional
//    public void configLinkedInAccount(){
//        log.info("Start config LinkedIn account");
//
//        openNotHeadlessBrowserIfNeed();
//
//        playwrightService.open("https://moveto.digital/boty_config.html");
//        playwrightService.sleepRandom(1000);
//        playwrightService.waitForSelector("#result > div");
//
//        linkedinAccount.setWorkInShabat(playwrightService.getElementByLocator("#work_in_shabat").get().isChecked());
//        linkedinAccount.setActiveSearch(playwrightService.getElementByLocator("#active_search").get().isChecked());
//        linkedinAccount.setPosition(playwrightService.getElementByLocator("#positions").get().inputValue());
//        linkedinAccount.setLocation(Location.valueOf(playwrightService.getElementByLocator("#location").get().inputValue()));
//        linkedinAccount = linkedinAccountService.save(linkedinAccount);
//
//        closeHeadlessBrowserIfNeed();
//    }
}
