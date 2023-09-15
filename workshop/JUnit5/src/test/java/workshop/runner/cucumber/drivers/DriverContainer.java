package workshop.runner.cucumber.drivers;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import workshop.runner.playwright.pages.EcFeedPageObject;

public class DriverContainer {
    private Playwright playwright;
    private Browser browser;

    private BrowserContext context;
    private Page page;

    public EcFeedPageObject ecfeed;

    public Playwright getPlaywright() {
        return playwright;
    }

    public void setPlaywright(Playwright playwright) {
        this.playwright = playwright;
    }

    public Browser getBrowser() {
        return browser;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public BrowserContext getContext() {
        return context;
    }

    public void setContext(BrowserContext context) {
        this.context = context;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public EcFeedPageObject getEcfeed() {
        return ecfeed;
    }

    public void setEcfeed(EcFeedPageObject ecfeed) {
        this.ecfeed = ecfeed;
    }
}
