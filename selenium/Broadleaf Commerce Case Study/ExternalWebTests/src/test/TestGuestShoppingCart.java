package test;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

import test.enums.PaymentMethod;
import test.enums.ShippingMethod;
import test.enums.State;
import tools.PageAddress;

import com.testify.ecfeed.junit.StaticRunner;
import com.testify.ecfeed.junit.annotations.EcModel;

@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestGuestShoppingCart extends ParentTest{

	protected String checkoutURL = "https://localhost:8443/checkout";

	public TestGuestShoppingCart(){
		fBaseUrl = PageAddress.BASE;
	}
	
	protected class ItemSpec{
		final String name;
		Integer quantity;
		BigDecimal price;
		BigDecimal discount;

		public ItemSpec(String aname){
			name = aname;
			quantity = null;
			price = null;
		}

		public BigDecimal getTotalPrice(){
			if(quantity == null || price == null){
				return new BigDecimal(0);
			}
			if(discount != null){
				return (price.multiply(new BigDecimal(quantity))).subtract(discount);
			} else {
				return price.multiply(new BigDecimal(quantity));
			}
		}

		@Override
		public boolean equals(Object o){
			if(o != null && o instanceof ItemSpec){
				return name.equals(((ItemSpec)o).name);
			} else{
				return false;
			}
		}
		
		@Override
		public int hashCode(){
	         if(name == null){
	        	 return 0;
	         } else{
	        	 return name.hashCode();
	         }
		}
		
	}
	
	@Test
	public void testAddToCart(String name) throws Exception{

		try{
			setUp();
			fDriver.get(fBaseUrl + "/");
			fDriver.findElement(By.name("q")).clear();
			fDriver.findElement(By.name("q")).sendKeys(name);
			fDriver.findElement(By.name("q")).sendKeys(Keys.ENTER);
			
			int itemCount = itemCount();
			
			Assert.assertTrue("no elements found", itemCount > 0 && isElementPresent(By.cssSelector("input.addToCart")));
			Random rng = new Random(System.currentTimeMillis());
			
			int index = rng.nextInt(itemCount) +1;
			//random
			
			String saucename = fDriver.findElement(By.xpath("//ul[@id='products']/li["+ index +"]//div[@class='title']")).getText();
			fDriver.findElement(By.xpath("//ul[@id='products']/li["+ index +"]//input[4]")).sendKeys(Keys.ENTER);
			
			tryAddGadgetToCart();
			
			for(int i = 0; i < 10; i++){
				fDriver.findElement(By.cssSelector("span.headerCartItemsCountWord")).sendKeys(Keys.HOME);
				fDriver.findElement(By.cssSelector("span.headerCartItemsCountWord")).click();	
				if(isElementPresent(By.xpath("id('cart_products')"))){
					break;
				}
			}
			Assert.assertTrue(isElementPresent(By.xpath("id('cart_products')")));
			Assert.assertTrue("cart doesn't contain selected item",
					fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[1]/td[2]/a")).getText().equalsIgnoreCase(saucename));
		} finally{
			tearDown();
		}
	}
	
	@Test
	public void testPurchaseItem(String itemName, boolean zeroItemsAllowed, int maxItemQuantity, boolean useBillingAddress, String email, String name,
			String lastname, String phone, String address1, String address2, String city, State state, String zipcode, ShippingMethod shippingMethod,
			PaymentMethod paymentMethod) throws Exception{
		try{
			setUp();

			LinkedHashSet<ItemSpec> items = new LinkedHashSet<>();
			BigDecimal sum = new BigDecimal(0);
			
			
			fDriver.navigate().to(fBaseUrl);
			fDriver.findElement(By.name("q")).clear();
			fDriver.findElement(By.name("q")).sendKeys(itemName);
			fDriver.findElement(By.id("search_button")).click();
			
			int itemCount = selectRandomItems(items);			
			
			Assert.assertTrue("item count doesn't match: selected " + itemCount + ", found " + fDriver.findElement(By.cssSelector("span.headerCartItemsCount")).getText(),
					(Integer.toString(itemCount)).equals(fDriver.findElement(By.cssSelector("span.headerCartItemsCount")).getText()));
			
			for(int i = 0; i < 10; i++){
				fDriver.findElement(By.cssSelector("span.headerCartItemsCountWord")).sendKeys(Keys.HOME);
				fDriver.findElement(By.cssSelector("span.headerCartItemsCountWord")).click();	
				if(isElementPresent(By.xpath("id('cart_products')"))){
					break;
				} else {
					Thread.sleep(100);
				}	
			}
			Assert.assertTrue(isElementPresent(By.xpath("id('cart_products')")));
			
			randomizeQuantity(items, maxItemQuantity, zeroItemsAllowed);
			boolean empty = (sum = evaluatePrice(items)).equals(new BigDecimal(0));
			
			Assert.assertTrue("Current subtotal and actual price doesn't match",
					(empty && !isElementPresent(By.id("subtotal"))) ||
					sum.toString().equals(fDriver.findElement(By.id("subtotal")).getText().replace("$", "")));
			
			fDriver.findElement(By.cssSelector("a.big-button.red-button > span")).click();
			
			if(empty){
				Assert.assertTrue("Should display note about empty cart", isElementPresent(By.xpath("id('cart')/div[@class='checkout_warning']/span[contains(.,'" + ErrorMessage.CartIsEmpty + "')]")));
				return;
			}
			
			Assert.assertEquals("Total checkout sum doesn't match", sum.toString(), fDriver.findElement(By.id("checkout_total")).getText().replace("$", ""));
	
			fDriver.findElement(By.id("emailAddress")).clear();
			fDriver.findElement(By.id("emailAddress")).sendKeys(email);
			fDriver.findElement(By.cssSelector("input.small.red")).sendKeys(Keys.ENTER);
			
			fDriver.findElement(By.id("address.firstName")).clear();
			fDriver.findElement(By.id("address.firstName")).sendKeys(name);
			fDriver.findElement(By.id("address.lastName")).clear();
			fDriver.findElement(By.id("address.lastName")).sendKeys(lastname);
			fDriver.findElement(By.id("address.phonePrimary")).clear();
			fDriver.findElement(By.id("address.phonePrimary")).sendKeys(phone);
			fDriver.findElement(By.id("address.addressLine1")).clear();
			fDriver.findElement(By.id("address.addressLine1")).sendKeys(address1);
			fDriver.findElement(By.id("address.addressLine2")).clear();
			fDriver.findElement(By.id("address.addressLine2")).sendKeys(address2);
			fDriver.findElement(By.id("address.city")).clear();
			fDriver.findElement(By.id("address.city")).sendKeys(city);
			new Select(fDriver.findElement(By.id("state"))).selectByVisibleText(state.toString());
			fDriver.findElement(By.id("address.postalCode")).clear();
			fDriver.findElement(By.id("address.postalCode")).sendKeys(zipcode);
			fDriver.findElement(By.cssSelector("input.medium.red")).sendKeys(Keys.ENTER);
			
			if(useBillingAddress){
				fDriver.findElement(By.id("use_billing_address")).click();
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.firstName'])[2]")).getAttribute("disabled").equals("true"));	
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.lastName'])[2]")).getAttribute("disabled").equals("true"));
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.phonePrimary'])[2]")).getAttribute("disabled").equals("true"));
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.addressLine1'])[2]")).getAttribute("disabled").equals("true"));
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.addressLine2'])[2]")).getAttribute("disabled").equals("true"));
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.city'])[2]")).getAttribute("disabled").equals("true"));
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.cssSelector("div.left_content > div.form30 > #state")).getAttribute("disabled").equals("true"));
				Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.postalCode'])[2]")).getAttribute("disabled").equals("true"));
			} else {
				fDriver.findElement(By.xpath("(//input[@id='address.firstName'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.firstName'])[2]")).sendKeys("Firstname");
				fDriver.findElement(By.xpath("(//input[@id='address.lastName'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.lastName'])[2]")).sendKeys("Lastname");
				fDriver.findElement(By.xpath("(//input[@id='address.phonePrimary'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.phonePrimary'])[2]")).sendKeys("691691310");
				fDriver.findElement(By.xpath("(//input[@id='address.addressLine1'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.addressLine1'])[2]")).sendKeys("Ulica");
				fDriver.findElement(By.xpath("(//input[@id='address.addressLine2'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.addressLine2'])[2]")).sendKeys("ulica2 ulica2");
				fDriver.findElement(By.xpath("(//input[@id='address.city'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.city'])[2]")).sendKeys("Miasto");
				new Select(fDriver.findElement(By.cssSelector("div.left_content > div.form30 > #state"))).selectByVisibleText("AZ");
				fDriver.findElement(By.xpath("(//input[@id='address.postalCode'])[2]")).clear();
				fDriver.findElement(By.xpath("(//input[@id='address.postalCode'])[2]")).sendKeys("50-420");
			}
			
			switch(shippingMethod){
			case EXPRESS:
				fDriver.findElement(By.id("fulfillmentOptionId3")).click();			
				break;
			case PRIORITY:
				fDriver.findElement(By.id("fulfillmentOptionId2")).click();
				break;
			case STANDARD:
				fDriver.findElement(By.id("fulfillmentOptionId1")).click();
				break;
			default:
				break;
			}
			fDriver.findElement(By.id("select_shipping")).click();
			
			if(shippingMethod.equals(ShippingMethod.NONE)){
				Assert.assertTrue("Warning about shipping method should appear",
						fDriver.findElement(By.cssSelector("span.error")).getText().contains(ErrorMessage.SelectShippingMethod));
				return;
			}
			
			switch(paymentMethod){
			case NONE:
			case CARD:
				fDriver.findElement(By.id("paymentMethod_cc")).click();
				fDriver.findElement(By.id("paymentMethod_cc")).click();
				fDriver.findElement(By.id("cardNumber")).clear();
				fDriver.findElement(By.id("cardNumber")).sendKeys("371449635398431");
				fDriver.findElement(By.id("securityCode")).clear();
				fDriver.findElement(By.id("securityCode")).sendKeys("703");
				fDriver.findElement(By.id("nameOnCard")).clear();
				fDriver.findElement(By.id("nameOnCard")).sendKeys("CardName");
				fDriver.findElement(By.id("cardExpDate")).clear();
				fDriver.findElement(By.id("cardExpDate")).sendKeys("02/17");
				fDriver.findElement(By.cssSelector("form > input.medium.red")).sendKeys(Keys.ENTER);		
				Assert.assertTrue("No confirmation displayed", isElementPresent(By.id("order_confirmation")));
				Assert.assertTrue("No confirmation displayed", isElementPresent(By.xpath("id('order_confirmation')//p[contains(.,'A confirmation email will be sent to "+ email + "')]")));
				Assert.assertFalse("Error present", isElementPresent(By.className("error")));
				Assert.assertFalse("Error present", isElementPresent(By.className("payment-error")));
				break;
			case PAYPAL:
				fDriver.findElement(By.id("paymentMethod_paypal")).click();
				break;
			case DELIVERY:
				fDriver.findElement(By.id("paymentMethod_cod")).click();
				fDriver.findElement(By.cssSelector("#complete_checkout > input.medium.red")).click();
				Assert.assertTrue("No confirmation displayed", isElementPresent(By.id("order_confirmation")));
				Assert.assertTrue("No confirmation displayed", isElementPresent(By.xpath("id('order_confirmation')//p[contains(.,'A confirmation email will be sent to "+ email + "')]")));
				Assert.assertFalse("Error present", isElementPresent(By.className("error")));
				Assert.assertFalse("Error present", isElementPresent(By.className("payment-error")));
				break;	
			}
		} finally{
			tearDown();
		}
	}
	
	private int itemCount(){
		int count = 1;
		while(isElementPresent(By.xpath("id('products')/li["+count+"]//input[4]"))){
			count ++;
		}
		return count-1;
	}
	
	private boolean tryAddGadgetToCart(){
		if(isElementPresent(By.id("simplemodal-container"))){
			Random rng = new Random(System.currentTimeMillis());
			int attributeCount = fDriver.findElements(By.xpath("id('simplemodal-container')//div/ul/li")).size();
			
			for(int i = 1; i <= attributeCount+1; i++){
				if(i == attributeCount +1){
					fDriver.findElement(By.xpath("id('simplemodal-container')//div/ul/li[" + (i-1) + "]")).sendKeys(Keys.END);				
					fDriver.findElement(By.xpath("id('simplemodal-container')//input[@class='addToCart']")).click();
					break;
				}
				try{
					int optionCount = fDriver.findElements(By.xpath("id('simplemodal-container')//div/ul/li[1]/ul/li")).size();
					int index = rng.nextInt(optionCount) + 1;
					fDriver.findElement(By.xpath("id('simplemodal-container')//div/ul/li["+ i +"]/ul/li[" + index + "]/div/a")).click();
				} catch(Exception e){
					
				}
			}
			return true;
		}
		return false;
	}
	
	private int selectRandomItems(Set<ItemSpec> items){
		Random rng = new Random(System.currentTimeMillis());	
		int itemCount = itemCount();
		
		int rollCount = rng.nextInt(itemCount) + 1;
		
		while(items.size() < rollCount){
			int i = rng.nextInt(itemCount) + 1;
			if(items.add(new ItemSpec(fDriver.findElement(By.xpath("//ul[@id='products']/li["+ i +"]//div[@class='title']")).getText()))){
				fDriver.findElement(By.xpath("//ul[@id='products']/li["+ i +"]//input[4]")).sendKeys(Keys.ENTER);
				tryAddGadgetToCart();
				try{
					Thread.sleep(100);
				} catch(Exception e){	
				}
			}
		}
		
		return rollCount;
	}
	
	private void randomizeQuantity(LinkedHashSet<ItemSpec> items, int maxItemQuantity, boolean zeroItemsAllowed){
		Random rng = new Random(System.currentTimeMillis());
		for(ItemSpec item: items){
			boolean notFound = true;
			for(int i= 1; i <= items.size(); i++){
				try{
					if(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr["+i+"]/td[2]/a")).getText().equalsIgnoreCase(item.name)){
						if(maxItemQuantity == 0){
							item.quantity = zeroItemsAllowed ? 0 : 1;
						} else{
							item.quantity = rng.nextInt(maxItemQuantity) + (zeroItemsAllowed ? 0 : 1);
						}
	
						fDriver.findElement(By.xpath("id('cart_products')/tbody/tr["+i+"]/td[3]/form/input[3]")).clear();
						fDriver.findElement(By.xpath("id('cart_products')/tbody/tr["+i+"]/td[3]/form/input[3]")).sendKeys(item.quantity.toString());
						fDriver.findElement(By.xpath("id('cart_products')/tbody/tr["+i+"]/td[3]/form/input[3]")).sendKeys(Keys.ENTER);
						notFound = false;
						try{
							Thread.sleep(200);
						} catch(Exception e){
						}
						break;
					}
				} catch(NoSuchElementException e){
					throw (new Error("Product not found in the cart"));
				}
			}
			if(notFound){
				throw (new Error("Product not found in the cart"));
			}
		}
	}
	
	private BigDecimal evaluatePrice(LinkedHashSet<ItemSpec> items){
		BigDecimal sum = new BigDecimal(0);

		for(ItemSpec item : items){
			if(item.quantity == 0){
				Assert.assertFalse("Product shouldn't be present having quantity of 0",
						isElementPresent(By.xpath("id('cart_products')/tbody//a[contains(.,'" + item.name + "')]")));
			} else{
				boolean notFound = true;
				for(int i = 1; i <= items.size(); i++){
					try{
						if(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + i + "]/td[2]/a")).getText().equalsIgnoreCase(item.name)){
							item.price = new BigDecimal(
											(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + i + "]/td[4]")).getText())
													.replace("$", ""));
							BigDecimal discount = null;
							try{
								discount = new BigDecimal(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + i + "]/td[5]/span")).getText().replace("$", ""));
								System.out.println("discount: " + discount.toString());
							} catch(NumberFormatException e){
							} catch(NoSuchElementException ex){			
							}
							if(discount != null){
								item.discount = discount;
							}

							BigDecimal total = item.getTotalPrice();
							Assert.assertTrue(
									"Price for " + item.name + " doesn't match: " + item.price.toString() + " times " + item.quantity.toString() + " == "
									+ total.toString() + ", found " + fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + i + "]/td[6]")).getText() + " !",
									total.toString().equals(
											(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + i + "]/td[6]")).getText())
													.replace("$", "")));
							sum = sum.add(total);
							notFound = false;
							try{
								Thread.sleep(100);
							} catch(Exception e){
							}
							break;
						}
					} catch(NoSuchElementException e){
						throw(new Error("Product not found in the cart"));
					}
				}
				if(notFound){
					throw(new Error("Product not found in the cart"));
				}
			}
		}
		
		return sum;
	}
	
}



