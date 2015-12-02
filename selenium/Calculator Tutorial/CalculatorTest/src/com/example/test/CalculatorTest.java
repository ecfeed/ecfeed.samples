package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CalculatorTest {

	public void testCalculator(
			String text1, String operationType, String text2, 
			String expectedResult, String expectedComment) {
		checkOperationType(operationType);
		WebDriver driver = new FirefoxDriver();
		driver.get("file:///someDirectory/Calculator/index.html");

		try {
			performTest(
					text1, operationType, text2, 
					expectedResult, expectedComment,
					driver);

		} finally {
			driver.quit();
		}
	}

	private void performTest(
			String text1, String operationType, String text2, 
			String expectedResult, String expectedComment,
			WebDriver driver) {

		fillFieldsAndCalculate(text1, operationType, text2, driver);

		if (!expectedResult.equals("irrelevant")) {
			WebElement resultElement = driver.findElement(By.name("result"));
			String result = resultElement.getAttribute("value");
			assertEquals(expectedResult, result);
		}

		if (!expectedComment.equals("irrelevant")) {
			WebElement commentElement = driver.findElement(By.name("comment"));
			String comment = commentElement.getAttribute("value");
			assertEquals(expectedComment, comment);
		}
	}

	private void fillFieldsAndCalculate(
			String text1, String operationType, String text2, WebDriver driver) {
		pause(0.3);
		WebElement input1Element = driver.findElement(By.name("input1"));
		input1Element.sendKeys(text1);

		pause(0.3);
		WebElement operationElement = driver.findElement(By.name("operation"));
		operationElement.sendKeys(operationType);

		pause(0.3);
		WebElement input2Element = driver.findElement(By.name("input2"));
		input2Element.sendKeys(text2);

		pause(0.3);
		WebElement calculateElement = driver.findElement(By.name("calculate"));
		calculateElement.click();
		pause(1);
	}

	private void checkOperationType(String operationType) {
		if (operationType.equals("+") || operationType.equals("-") 
				|| operationType.equals("x") || operationType.equals("/")) {
			return;
		}
		fail("Invalid operation Type");
	}

	private void pause(double seconds) {
//		try {
//			Thread.sleep((long) (1000 * seconds));
//		} catch(InterruptedException ex) {
//			Thread.currentThread().interrupt();
//		}		
	}
}