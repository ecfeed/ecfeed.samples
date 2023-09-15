package workshop.runner.cucumber.definitions.base;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import workshop.runner.cucumber.drivers.DriverFactory;

public class Hooks {

    @Before
    public void beforeScenario() {
        DriverFactory.setUp();
    }

    @After
    public void afterScenario() {
        DriverFactory.tearDown();
    }
}
