package test;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import tools.DataSourceFactory;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;
import com.testify.ecfeed.runner.annotations.TestSuites;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestRegisterBasic{
	private WebDriver driver = null;
	private String baseUrl;
	private String baseRedirectUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	private Connection connection;
	private Statement statement;

	private boolean setUpEnv = false;
	private boolean tearDown;

	private String loginNotFoundError = "The e-mail address and/or password entered do not match our records. Please try again";
	private String login_taken_error = "email address is already in use";

	// @BeforeClass
	public void setUpEnvironment(){
		try{
			connection = DataSourceFactory.getHSQLDataSource().getConnection();
			statement = connection.createStatement();
		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Failed to initialize database connection");
		}

		if(driver == null){
			try{
				driver = new FirefoxDriver();
				baseUrl = "http://localhost:8080/";
				baseRedirectUrl = "https://localhost:8443/register";
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			} catch(Exception e){
				throw new Error("Failed to initialize Selenium driver");
			}
		}

		setUpEnv = true;
		tearDown = true;
	}

	// @Before
	public void setUp(){
		driver.get(baseUrl);
	}

	// @After
	public void tearDown() throws Exception{

	}

	// @AfterClass
	public void tearDownEnvironment(){
		try{
			if(connection != null)
				connection.close();
		} catch(SQLException e){
			e.printStackTrace();
		}

		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if(!"".equals(verificationErrorString)){
			fail(verificationErrorString);
		}
		setUpEnv = false;
		tearDown = true;
	}

	@Test
	@TestSuites("valid data")
	public void testRegisterValidData(String email, String first_name, String last_name, String password) throws Exception{
		if(!setUpEnv){
			setUpEnvironment();
		}
		setUp();
		driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
		driver.findElement(By.id("customer.emailAddress")).clear();
		driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
		driver.findElement(By.id("customer.firstName")).clear();
		driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
		driver.findElement(By.id("customer.lastName")).clear();
		driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("passwordConfirm")).clear();
		driver.findElement(By.id("passwordConfirm")).sendKeys(password);
		driver.findElement(By.xpath("//input[@value='Register']")).click();

		boolean email_taken = isElementPresent(By.xpath("//*[contains(.,'" + login_taken_error + "')]"));
		if(email_taken){
			driver.findElement(By.cssSelector("span")).click();
			driver.findElement(By.name("j_username")).clear();
			driver.findElement(By.name("j_username")).sendKeys(email);
			driver.findElement(By.name("j_password")).clear();
			driver.findElement(By.name("j_password")).sendKeys(password);
			driver.findElement(By.xpath("//input[@value='Login']")).click();
		}
		Assert.assertTrue("Not logged in - registration failed!", driver.findElement(By.linkText("Logout")) != null);

		// revert password change and logout
		driver.findElement(By.cssSelector("a > span")).click();
		Assert.assertTrue("Not logged out!", driver.findElement(By.linkText("Login")) != null);
		cleanUpAfterTest(email);

		// ERROR: Caught exception [ERROR: Unsupported command [selectWindow |
		// null | ]]
	}

	@Test
	public void testRegisterEmailData(String email, String first_name, String last_name, String password, boolean expected_result){
		if(!setUpEnv){
			setUpEnvironment();
		}
		driver.get(baseRedirectUrl);

		driver.findElement(By.id("customer.emailAddress")).clear();
		driver.findElement(By.id("customer.emailAddress")).sendKeys(email);

		driver.findElement(By.id("customer.firstName")).clear();
		driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
		driver.findElement(By.id("customer.lastName")).clear();
		driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("passwordConfirm")).clear();
		driver.findElement(By.id("passwordConfirm")).sendKeys(password);

		driver.findElement(By.xpath("//input[@value='Register']")).click();

		if(!expected_result){
			Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'email address is invalid')]"))
					|| isElementPresent(By.xpath("//*[contains(.,' Please enter your email address.'')]")));
		} else{
			Assert.assertTrue(isElementPresent(By.xpath("//*[contains(.,'email address is already in use')]"))
					|| driver.findElement(By.linkText("Logout")) != null
					|| !isElementPresent(By.xpath("//*[contains(.,'email address is invalid')]"))
					|| !isElementPresent(By.xpath("//*[contains(.,' Please enter your email address.'')]")));
		}

		cleanUpAfterTest(email);
	}

	@Test
	public void testRegisterBasic(String email, String first_name, String last_name, String password, String confpsswd) throws Exception{
		if(!setUpEnv){
			setUpEnvironment();
		}
		setUp();

		driver.findElement(By.xpath("//div[@id='cart_info']/a[2]/span")).click();
		driver.findElement(By.id("customer.emailAddress")).clear();
		driver.findElement(By.id("customer.emailAddress")).sendKeys(email);
		driver.findElement(By.id("customer.firstName")).clear();
		driver.findElement(By.id("customer.firstName")).sendKeys(first_name);
		driver.findElement(By.id("customer.lastName")).clear();
		driver.findElement(By.id("customer.lastName")).sendKeys(last_name);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("passwordConfirm")).clear();
		driver.findElement(By.id("passwordConfirm")).sendKeys(confpsswd);
		driver.findElement(By.xpath("//input[@value='Register']")).click();
		driver.findElement(By.linkText("Proper Name")).click();
		driver.findElement(By.cssSelector("a > span")).click(); // ERROR: Caught
		// exception [ERROR: Unsupported command [selectWindow | null | ]]
		cleanUpAfterTest(email);
	}

	private void cleanUpAfterTest(String email){
		try{
			statement.executeQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE;");
			statement.executeUpdate("DELETE FROM PUBLIC.blc_customer WHERE EMAIL_ADDRESS='" + email + "';");
			statement.executeQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE;");
		} catch(Exception e){
			throw new Error("Database connection failed");
		}
	}

	private boolean isElementPresent(By by){
		try{
			driver.findElement(by);
			return true;
		} catch(NoSuchElementException e){
			return false;
		}
	}

	private boolean isAlertPresent(){
		try{
			driver.switchTo().alert();
			return true;
		} catch(NoAlertPresentException e){
			return false;
		}
	}

	private String closeAlertAndGetItsText(){
		try{
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if(acceptNextAlert){
				alert.accept();
			} else{
				alert.dismiss();
			}
			return alertText;
		} finally{
			acceptNextAlert = true;
		}
	}
}
