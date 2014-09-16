package test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.browserlaunchers.Sleeper;

import tools.PageAddress;

import com.testify.ecfeed.runner.StaticRunner;
import com.testify.ecfeed.runner.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestSauceSearch extends ParentTest{
	
	public TestSauceSearch(){
		baseUrl = PageAddress.Base;
	}

	@Test
	public void testSearchSauce(String name, boolean expected) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);

			driver.findElement(By.name("q")).clear();
			driver.findElement(By.name("q")).sendKeys(name);
			driver.findElement(By.id("search_button")).click();
			if (expected) {
				assertTrue(isElementPresent(By.cssSelector("div.title")));
				driver.findElement(By.cssSelector("input.addToCart")).click();
				Sleeper.sleepTightInSeconds(2);
				assertNotEquals("0", driver.findElement(By.cssSelector("span.headerCartItemsCount")).getText());
			} else {
				assertTrue(!isElementPresent(By.cssSelector("div.title")));
			}
		} finally {
			tearDown();
		}
	}
	
	@Test
	public void testSearchKeywords(String keywords) throws Exception {
		try {
			setUp();
			driver.get(baseUrl);

			driver.findElement(By.name("q")).clear();
			driver.findElement(By.name("q")).sendKeys(keywords);
			driver.findElement(By.id("search_button")).click();
			
			int page = 1;
			int position = 1;
			while(page > 0){
				while(position > 0){
					if(isElementPresent(By.xpath("id('products')/li["+position+"]/div[1]/a"))){
						assertTrue("not found any keyword in resulting page!",
								openInNewTabAndScanForKeywords(driver.findElement(By.xpath("id('products')/li["+position+"]/div[1]/a")), keywords));
						position +=1;
						System.out.println("NORMAL");
					} else if(isElementPresent(By.xpath("id('products')/li["+position+"]/div[2]/a"))){
						assertTrue("not found any keyword in resulting page!",
								openInNewTabAndScanForKeywords(driver.findElement(By.xpath("id('products')/li["+position+"]/div[2]/a")), keywords));
						position +=1;
						System.out.println("NORMAL");
					} else {
						System.out.println("PAGE ENDED");	
						position = 1;
						page += 1;
						break;
					}	
				}
				if(isElementPresent(By.xpath("id('left_column')/div/ul/li["+page+"]/a"))){
					driver.findElement(By.xpath("id('left_column')/div/ul/li["+page+"]/a")).click();
					position = 1;
				} else{
					break;
				}
			}
			
		}
		finally {
		
			tearDown();
		}
	}
	
	protected boolean openInNewTabAndScanForKeywords(WebElement element, String keywords){
		boolean containsAny = false;
		WebDriver secondDriver = tools.DriverFactory.getDriver();
		String[] searchKeys = keywords.split("\\s+");
		try{
			secondDriver.navigate().to(element.getAttribute("href"));
			for(String s: searchKeys){
				if(StringUtils.containsIgnoreCase(secondDriver.findElement(By.id("description")).getText(), s)
						|| StringUtils.containsIgnoreCase(secondDriver.findElement(By.cssSelector("h2")).getText(), s)){
					containsAny = true;
					break;
				}				
			}

		} finally{
			secondDriver.close();
		}

		return containsAny;
	}
	

}
