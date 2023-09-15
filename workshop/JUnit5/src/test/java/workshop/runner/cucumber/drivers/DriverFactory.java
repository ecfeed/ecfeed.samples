package workshop.runner.cucumber.drivers;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import workshop.runner.playwright.pages.EcFeedPageObject;

public class DriverFactory {
    private static final ThreadLocal<DriverContainer> container = new ThreadLocal<>();

    public static DriverContainer get() {

        if (DriverFactory.container.get() == null) {
            throw new RuntimeException("The driver has not been initialized!");
        }

        return DriverFactory.container.get();
    }

    public static void setUp() {
        var container = new DriverContainer();

        var optionsBrowser = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setTimeout(5000);

        var optionsContext = new Browser.NewContextOptions()
                .setViewportSize(1536, 900);

        container.setPlaywright(Playwright.create());

        container.setBrowser(container.getPlaywright().firefox().launch(optionsBrowser));

        container.setContext(container.getBrowser().newContext(optionsContext));

        container.setPage(container.getContext().newPage());

        container.setEcfeed(new EcFeedPageObject(container.getPage()));

        DriverFactory.container.set(container);
    }

    public static void tearDown() {

        if (DriverFactory.container.get() == null) {
            return;
        }

        var container = DriverFactory.container.get();

        container.getContext().close();
        container.getPlaywright().close();
    }
}
