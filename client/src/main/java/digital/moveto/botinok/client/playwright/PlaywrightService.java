package digital.moveto.botinok.client.playwright;

import com.microsoft.playwright.*;
import digital.moveto.botinok.client.config.ClientConst;
import digital.moveto.botinok.client.config.GlobalConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service
@NoArgsConstructor
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PlaywrightService implements AutoCloseable {

    Logger log = LoggerFactory.getLogger(PlaywrightService.class);

    private Playwright playwright;
    private Page page;
    private Path userDataDir;
    private BrowserContext context;

    public Page getPage() {
        return page;
    }


    @Autowired
    private GlobalConfig globalConfig;

    public void start(Path userDateDir, boolean headless) {
        playwright = Playwright.create();
        BrowserType chromium = playwright.chromium();
        context = chromium
                .launchPersistentContext(userDateDir, new BrowserType.LaunchPersistentContextOptions().setHeadless(headless).setViewportSize(1366, 768));
        this.userDataDir = userDateDir;

        this.page = context.pages().get(0);
        this.page.setDefaultTimeout(ClientConst.DEFAULT_TIMEOUT_FOR_BROWSER);
        this.page.setDefaultNavigationTimeout(ClientConst.DEFAULT_TIMEOUT_FOR_BROWSER);
    }

    public void open(String url) {
        page.navigate(url);
    }

    public void openInNewPage(String url) {
        if (page.url() == null) {
            page.navigate(url);
        } else {
            Page newPage = context.newPage();
            newPage.navigate(url);
        }
    }


    public String getCurrentUrl() {
        return page.url();
    }

    public Response reload() {
        return page.reload();
    }

    public void close() {
        if (this.playwright != null) {
            this.playwright.close();
        }
        this.playwright = null;
        this.page = null;
    }

    public void click(String selector) {
        page.click(selector);
    }

    public void click(int x, int y) {
        page.mouse().click(x, y);
    }

    public void type(String selector, String text) {
        page.fill(selector, text);
    }

    public void screenshot(String fileName) {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(fileName)));
    }

    public void screenshot(String url, String path) {
        page.navigate(url);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
    }

    public void waitForSelector(String selector) {
        page.waitForSelector(selector);
    }

    public void waitForSelector(String selector, int timeout) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
    }

    public boolean isSelectorVisible(String selector) {
        return page.isVisible(selector);
    }

    public boolean isSelectorFind(String selector) {
        return page.locator(selector).count() > 0;
    }

    public boolean isTextFind(String selector) {
        return getLocator("text=" + selector).count() > 0;
    }

    public Locator getLocator(String selector) {
        return page.locator(selector);
    }

    public Optional<ElementHandle> getElementWithCurrentText(String text) {
        Optional<ElementHandle> first = getLocator("text=" + text).elementHandles().stream()
                .filter(elementHandle -> elementHandle.innerText().trim().equalsIgnoreCase(text))
                .findFirst();
        return first;
    }

    public Optional<ElementHandle> getElementByLocator(String locator) {
        Optional<ElementHandle> first = getLocator(locator).elementHandles().stream()
                .findFirst();
        return first;
    }

    public List<ElementHandle> getElementsByLocator(String locator) {
        return new ArrayList<>(getLocator(locator).elementHandles());
    }

    public Optional<ElementHandle> getByText(String text) {
        Optional<ElementHandle> first = page.getByText(text).elementHandles().stream()
                .findFirst();
        return first;
    }

    public Collection<ElementHandle> getElementsWithCurrentText(String text) {
        return getLocator("text=" + text).elementHandles().stream()
                .filter(elementHandle -> elementHandle.innerText().trim().equalsIgnoreCase(text))
                .collect(Collectors.toList());
    }

    public Collection<ElementHandle> getElementsWithText(String text) {
        Collection<ElementHandle> first = getLocator("text=" + text).elementHandles().stream()
                .filter(elementHandle -> elementHandle.innerText().trim().contains(text))
                .collect(Collectors.toList());
        return first;
    }


    public void waitForNavigation(Runnable code) {
        page.waitForNavigation(code);
    }

    public void waitForNavigation(Runnable code, int timeout) {
        page.waitForNavigation(new Page.WaitForNavigationOptions().setTimeout(timeout), code);
    }

    public void waitForLoadState() {
        page.waitForLoadState();
    }

    public void sleep(int timeout) {
        sleep(timeout, true);
    }
    public void sleep(int timeout, boolean changeSpeedOfBot) {
        if (changeSpeedOfBot) {
            timeout = (int) (timeout / globalConfig.speedOfBot);
        }
        if (timeout > 60_000) {
            log.debug("Sleep for {}:{} min.", timeout / 60_000, (timeout / 1000) % 60);
        } else {
            if (timeout > 1000) {
                log.trace("Sleep for {} sec.", timeout / 1000);
            }
        }
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sleepRandom(int min, int max) {
        sleep((int) (Math.random() * (max - min) + min));
    }

    public void sleepRandom(int timeout) {
        sleep((int) (timeout / 2 + Math.random() * timeout));
    }

    public void sleep(int timeout, String message) {
        log.info(message);
        sleep(timeout);
    }

    public String innerHTML(String selector) {
        return page.querySelector(selector).innerHTML();
    }

    public void scrollToSelector(String selector) {
        Locator locator = getLocator(selector);
        locator.scrollIntoViewIfNeeded();
    }

    public void scrollToSelector(Locator locator) {
        locator.scrollIntoViewIfNeeded();
    }

    public void scrollMouseDown(int x, int y, int deltaY) {
        scrollMouse(x, y, 0, deltaY);
    }

    public void scrollMouse(int x, int y, int deltaX, int deltaY) {
        page.mouse().move(x, y);
        page.mouse().wheel(deltaX, deltaY);
    }

}
