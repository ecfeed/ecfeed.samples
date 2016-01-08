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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import test.enums.PaymentMethod;
import test.enums.ShippingMethod;
import test.enums.State;
import tools.PageAddress;

import com.testify.ecfeed.junit.StaticRunner;
import com.testify.ecfeed.junit.annotations.EcModel;
import com.testify.ecfeed.junit.annotations.TestSuites;

// REMARKS. 
// Refactoring of this file is still needed: 
//   WebElements should be placed in PageObjects. 
//   Similar functionality should be extracted to common functions.
//   Randomization existing in methods - to be discussed if this is the right approach
//  
//   Found Broadleaf issues:
//     -  The testPurchaseItem for shirts does not pass. After selecting shirt's color and size (and typing personalized name) 
//        and clicking the button BUY NOW!, Broadleaf displays an error: An error occurred while processing your request.


@RunWith(StaticRunner.class)
@EcModel("src/model.ect")
public class TestGuestShoppingCart extends ParentTest{

	protected String checkoutURL = "https://localhost:8443/checkout";

	public TestGuestShoppingCart(){
		fBaseUrl = PageAddress.BASE;
	}

	protected class Item {
		private final String fName;
		private Integer fQuantity;
		private BigDecimal fPrice;
		private BigDecimal fDiscount;

		public Item(String name){
			fName = name;
			fQuantity = 0;
			fPrice = new BigDecimal(0);
		}
		
		public Item(String name, BigDecimal price){
			fName = name;
			fQuantity = 0;
			fPrice = price;
		}		

		public BigDecimal getTotalPrice(){
			if(fQuantity == null || fPrice == null){
				return new BigDecimal(0);
			}
			if(fDiscount != null){
				return (fPrice.multiply(new BigDecimal(fQuantity))).subtract(fDiscount);
			} else {
				return fPrice.multiply(new BigDecimal(fQuantity));
			}
		}

		@Override
		public boolean equals(Object o){
			if(o != null && o instanceof Item){
				return fName.equals(((Item)o).fName);
			} else{
				return false;
			}
		}

		@Override
		public int hashCode(){
			if(fName == null){
				return 0;
			} else{
				return fName.hashCode();
			}
		}

	}

	@Test
	@TestSuites("Failed")
	public void testAddToCart(String name) throws Exception{
		try{
			setUp();
			fDriver.get(fBaseUrl);

			filterItems(name);
			addRandomItemToCart();

			WebDriverWait wait = new WebDriverWait(fDriver, 5);
			wait.until(ExpectedConditions.textToBePresentInElement(getCartItemsCount(), "1"));
		} finally{
			tearDown();
		}
	}

	private void filterItems(String name) {
		fDriver.findElement(By.name("q")).clear();
		fDriver.findElement(By.name("q")).sendKeys(name);
		fDriver.findElement(By.name("q")).sendKeys(Keys.ENTER);
	}

	private void addRandomItemToCart() {
		int itemCount = itemsCount();
		Assert.assertTrue("No items found.", itemCount > 0 && isElementPresent(By.cssSelector("input.addToCart")));

		int itemNumber  = getRandomItemNumber(itemCount);
		selectAndAddItemToCart(itemNumber);
	}

	private void selectAndAddItemToCart(int itemNumber) {
		selectItem(itemNumber);
		Assert.assertEquals("The cart should be empty.", "0", getCartItemsCount().getText());
		addItemToCart();		
	}
	
	private int getRandomItemNumber(int itemCount) {
		Random random = new Random(System.currentTimeMillis());
		int index = random.nextInt(itemCount) +1;
		return index;
	}

	private void selectItem(int index) {
		String title = findItemTitle(index);

		WebElement itemAddress = findItemAddress(index);
		Assert.assertNotNull("Item address must not be null.", itemAddress);
		itemAddress.click();

		WebDriverWait wait = new WebDriverWait(fDriver, 5);
		wait.until(ExpectedConditions.titleContains("Broadleaf Commerce Demo Store - Heat Clinic - " + title));
	}

	private String findItemTitle(int index) {
		WebElement item = fDriver.findElement(By.xpath("//ul[@id='products']/li["+ index +"]//div[@class='title']"));
		return item.getText();
	}

	private WebElement findItemAddress(int index) {
		return fDriver.findElement(By.xpath("//ul[@id='products']/li["+ index +"]//a"));
	}

	private WebElement getCartItemsCount() {
		return fDriver.findElement(By.xpath("//span[@class='headerCartItemsCount']"));
	}

	private boolean addItemToCart(){
		selectRandomOptions();
		setPersonalizedName();

		WebElement submitElem = getSubmitButton(); 
		submitElem.click();

		return true;
	}

	private void  selectRandomOptions() {
		Random random = new Random(System.currentTimeMillis());
		int attributeCount = getAttributeCount();

		for (int attributeNumber = 1; attributeNumber <= attributeCount;  attributeNumber++) {
			int optionCount = getOptionCount(attributeNumber);
			int index = random.nextInt(optionCount) + 1;
			WebElement element = getOptionElement(attributeNumber, index);
			element.click();
		}
	}

	private int getAttributeCount(){
		int attributeCount = 0;

		try {
			attributeCount = fDriver.findElements(By.xpath("//div[@id='product_content']//div[@class='product-options']/ul/li")).size();
		}  catch(Exception ex) {
			return 0;
		}

		if (getPersonalizedName() != null) {
			attributeCount--;
		}

		return attributeCount;
	}

	private WebElement getPersonalizedName() {
		WebElement inputElement;
		try {
			int cnt = fDriver.findElements(By.xpath("//div[@id='product_content']//input[@name='itemAttributes[NAME]']")).size();

			if (cnt == 0) {
				return null;
			}

			inputElement = fDriver.findElement(By.xpath("//div[@id='product_content']//input[@name='itemAttributes[NAME]']"));
		} catch (NoSuchElementException ex) {
			return null;
		}
		return inputElement;
	}

	private int getOptionCount(int attribute) {
		String xpath = "//div[@class='product-options']/ul/li[" + attribute + "]/ul/li";
		return fDriver.findElements(By.xpath(xpath)).size();
	}

	private WebElement getOptionElement(int attribute, int attributeIndex) {
		String xpath = "//div[@class='product-options']/ul/li[" + attribute + "]/ul/li[" + attributeIndex + "]/div/a";
		WebElement element = fDriver.findElement(By.xpath(xpath));
		return element;
	}

	private void setPersonalizedName() {
		WebElement inputElement = getPersonalizedName();

		if (inputElement == null) {
			return;
		}
		inputElement.sendKeys("Xyz");
	}

	private WebElement getSubmitButton() {
		WebElement submitElem = fDriver.findElement(By.xpath("//input[@class='addToCart big red']"));
		return submitElem;
	}

	@Test
	@TestSuites("Failed")
	public void testPurchaseItem(String itemName, boolean zeroItemsAllowed, int maxItemQuantity, 
				boolean useBillingAddress, 
				String email, String name,
				String lastname, String phone, 
				String address1, String address2, String city, State state, String zipcode, 
				ShippingMethod shippingMethod, PaymentMethod paymentMethod) throws Exception{
		try{
			setUp();
			fDriver.get(fBaseUrl);

			filterItems(itemName);

			LinkedHashSet<Item> items = new LinkedHashSet<>();
			addRandomItemsToCart(items);			

			displayCart();
			randomizeItemsQuantity(items, maxItemQuantity, zeroItemsAllowed);

			BigDecimal sum = calculateTotalPrice(items);
			boolean isEmpty = sum.equals(new BigDecimal(0));
			checkSubtotal(isEmpty, sum.toString());

			clickCheckoutButton();
			if (isEmpty) {
				checkEmptyCartWarningExists();
				return;
			}
			checkCheckoutTotal(sum.toString());
			setAndCheckTransactionData(
					useBillingAddress, email, 
					name, lastname, 
					phone, address1, address2, city, state,	zipcode, 
					shippingMethod,	paymentMethod);
		} finally{
			tearDown();
		}
	}
	
	private void setAndCheckTransactionData(
			boolean useBillingAddress, String email, 
			String name, String lastname, String phone, String address1, String address2, 
			String city, State state, String zipcode, 
			ShippingMethod shippingMethod, PaymentMethod paymentMethod) {
		
		fillEmailAddress(email);
		fillAddres(name, lastname, phone, address1, address2, city, state, zipcode);

		if (useBillingAddress) {
			checkAddressDisabled();
		} else {
			fillAddress2();
		}

		setAndCheckShippingMethod(shippingMethod);
		setAndCheckPaymentMethod(email, paymentMethod);
	}

	private void setAndCheckPaymentMethod(String email, PaymentMethod paymentMethod) {
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
	}
	
    private void setAndCheckShippingMethod(ShippingMethod shippingMethod) {
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
    }
    
	private void fillAddress2() {
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
	
	private void checkAddressDisabled() {
		fDriver.findElement(By.id("use_billing_address")).click();
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.firstName'])[2]")).getAttribute("disabled").equals("true"));	
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.lastName'])[2]")).getAttribute("disabled").equals("true"));
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.phonePrimary'])[2]")).getAttribute("disabled").equals("true"));
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.addressLine1'])[2]")).getAttribute("disabled").equals("true"));
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.addressLine2'])[2]")).getAttribute("disabled").equals("true"));
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.city'])[2]")).getAttribute("disabled").equals("true"));
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.cssSelector("div.left_content > div.form30 > #state")).getAttribute("disabled").equals("true"));
		Assert.assertTrue("Field should be disabled", fDriver.findElement(By.xpath("(//input[@id='address.postalCode'])[2]")).getAttribute("disabled").equals("true"));
	}
	
	private void fillAddres(String name, String lastname, String phone, String address1, String address2, 
							String city, State state, String zipcode) {
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
		new Select(fDriver.findElement(By.id("state"))).selectByVisibleText("AA"); // parameter state ignored in this version
		fDriver.findElement(By.id("address.postalCode")).clear();
		fDriver.findElement(By.id("address.postalCode")).sendKeys(zipcode);
		fDriver.findElement(By.cssSelector("input.medium.red")).sendKeys(Keys.ENTER);
	}
	
	private void fillEmailAddress(String email) {
		fDriver.findElement(By.id("emailAddress")).clear();
		fDriver.findElement(By.id("emailAddress")).sendKeys(email);
		fDriver.findElement(By.cssSelector("input.small.red")).sendKeys(Keys.ENTER);
	}
	
	private void checkCheckoutTotal(String sumStr) {
		WebElement checkoutTotal = fDriver.findElement(By.id("checkout_total"));
		Assert.assertEquals("Total checkout sum doesn't match", sumStr, checkoutTotal.getText().replace("$", ""));
	}
	
	private void clickCheckoutButton() {
		fDriver.findElement(By.cssSelector("a.big-button.red-button > span")).click();
	}
	
	private void checkSubtotal(boolean isEmpty, String sumStr) {
		Assert.assertTrue("Current subtotal and actual price doesn't match",
				(isEmpty && !isElementPresent(By.id("subtotal"))) ||
				sumStr.equals(fDriver.findElement(By.id("subtotal")).getText().replace("$", "")));
	}
	
	private void checkSelectedItemsCount(int selectedItems) {
		sleepMilliseconds(100);
		WebElement cartItemsCountHeader = fDriver.findElement(By.cssSelector("span.headerCartItemsCount"));
		String headerText = cartItemsCountHeader.getText();
		String message = "Item count doesn't match: selected " + selectedItems + ", found " + cartItemsCountHeader.getText() + ".";
		Assert.assertTrue(message, (Integer.toString(selectedItems)).equals(headerText));
	}
	
	private void checkEmptyCartWarningExists() {
		String carIsEmptyXpath = "id('cart')/div[@class='checkout_warning']/span[contains(.,'" + ErrorMessage.CartIsEmpty + "')]";
		Assert.assertTrue("Should display note about empty cart", isElementPresent(By.xpath(carIsEmptyXpath)));
	}
	
	private void displayCart() {
		int MAX_ATTEMPTS = 10;
		
		for(int attempt = 0; attempt < MAX_ATTEMPTS; attempt++){
			fDriver.findElement(By.cssSelector("span.headerCartItemsCountWord")).sendKeys(Keys.HOME);
			fDriver.findElement(By.cssSelector("span.headerCartItemsCountWord")).click();	
			if(isElementPresent(By.xpath("id('cart_products')"))){
				break;
			} else {
				sleepMilliseconds(100);
			}	
		}
		Assert.assertTrue(isElementPresent(By.xpath("id('cart_products')")));
	}

	private int itemsCount(){
		int count = 1;
		while(isElementPresent(By.xpath("id('products')/li["+count+"]//input[4]"))){
			count ++;
		}
		return count-1;
	}

	private void addRandomItemsToCart(Set<Item> items){
		Random random = new Random(System.currentTimeMillis());
		int itemCount = itemsCount();
		int itemsInCart = random.nextInt(itemCount) + 1;

		while(items.size() < itemsInCart){
			int itemNumber = random.nextInt(itemCount) + 1;
			Item newItem = createItem(itemNumber);
			
			if(items.add(newItem)){
				WebElement addToCartButton = fDriver.findElement(By.xpath("//ul[@id='products']/li["+ itemNumber +"]//input[4]"));  
				addToCartButton.sendKeys(Keys.ENTER);
				if (modalWindowDisplayed()) {
					selectRandomOptionsModal();
					addItemWithOptionsToCartModal();
				}
				sleepMilliseconds(100);
			}
		}
		checkSelectedItemsCount(itemsInCart);
	}
	
	private boolean modalWindowDisplayed() {
		if (1 == fDriver.findElements(By.xpath("//div[@class='product-options modal simplemodal-data']")).size()) {
			return true;
		}
		return false;
	}
	
	private void addItemWithOptionsToCartModal() {
		WebElement element = fDriver.findElement(By.xpath("//div[@class='product-options modal simplemodal-data']//input[@class='addToCart']"));
		element.click();
	}
	
	private void  selectRandomOptionsModal() {
		Random random = new Random(System.currentTimeMillis());
		int attributeCount = getAttributeCountModal();

		for (int attributeNumber = 1; attributeNumber <= attributeCount;  attributeNumber++) {
			int optionCount = getOptionCountModal(attributeNumber);
			int index = random.nextInt(optionCount) + 1;
			WebElement element = getOptionElementModal(attributeNumber, index);
			element.click();
		}
	}
	
	private int getAttributeCountModal(){
		int attributeCount = 0;

		try {
			attributeCount = fDriver.findElements(By.xpath("//div[@class='product-options modal simplemodal-data']/div/ul/li")).size();
		}  catch(Exception ex) {
			return 0;
		}

		if (getPersonalizedNameModal() != null) {
			attributeCount--;
		}

		return attributeCount;
	}	
	
	private WebElement getPersonalizedNameModal() {
		WebElement inputElement;
		try {
			int cnt = fDriver.findElements(By.xpath("//div[@class='product-options modal simplemodal-data']//input[@name='itemAttributes[NAME]']")).size();

			if (cnt == 0) {
				return null;
			}

			inputElement = fDriver.findElement(By.xpath("//div[@class='product-options modal simplemodal-data']//input[@name='itemAttributes[NAME]']"));
		} catch (NoSuchElementException ex) {
			return null;
		}
		return inputElement;
	}	
	
	private int getOptionCountModal(int attribute) {
		String xpath = "//div[@class='product-options modal simplemodal-data']/div/ul/li[" + attribute + "]/ul/li";
		return fDriver.findElements(By.xpath(xpath)).size();
	}
	
	private WebElement getOptionElementModal(int attribute, int attributeIndex) {
		String xpath = "//div[@class='product-options modal simplemodal-data']/div/ul/li[" + attribute + "]/ul/li[" + attributeIndex + "]/div/a";
		WebElement element = fDriver.findElement(By.xpath(xpath));
		return element;
	}	
	
	private Item createItem(int itemNumber) {
		String itemTitle = fDriver.findElement(By.xpath("//ul[@id='products']/li["+ itemNumber +"]//div[@class='title']")).getText();
		BigDecimal itemPrice = getItemPrice1(itemNumber);
		return new Item(itemTitle, itemPrice);
	}
	
	private BigDecimal getItemPrice1(int itemNumber) {
		String priceXpath = "//ul[@id='products']/li["+ itemNumber +"]//div[@class='price']";
		String priceSaleXpath = priceXpath + "//div[@class='sale']";
		
		if (elementExistsByXpath(priceSaleXpath)) {
			return createItemPrice(priceSaleXpath);
		}
		return createItemPrice(priceXpath); 
	}
	
	private BigDecimal createItemPrice(String xpath) {
		WebElement element = fDriver.findElement(By.xpath(xpath));
		String text = element.getText();
		text = text.replace("$", "");
		return new BigDecimal(text);
	}
	
	private boolean elementExistsByXpath(String xpath) {
		try {
			fDriver.findElement(By.xpath(xpath));
			return true;
		} catch (NoSuchElementException ex) {
			return false;
		}
	}

	private void randomizeItemsQuantity(LinkedHashSet<Item> items, int maxItemQuantity, boolean zeroItemsAllowed){
		Random random = new Random(System.currentTimeMillis());
		int itemsSize = items.size();

		for(Item item: items) {
			randomizeQuantityOfItem(item, maxItemQuantity, zeroItemsAllowed, itemsSize, random);
		}
	}

	private void randomizeQuantityOfItem(Item item, int maxItemQuantity, boolean zeroItemsAllowed, int itemsSize, Random random) {
		boolean found = false;

		for(int itemPosition = 1; itemPosition <= itemsSize; itemPosition++){
			if (findItemAndSetQuantity(itemPosition, item, maxItemQuantity, zeroItemsAllowed, random)) {
				found = true;
				break;
			}
		}

		if(!found){
			throw (new Error("Product not found in the cart"));
		}
	}

	private boolean findItemAndSetQuantity(int itemPosition, Item item, 
			int maxItemQuantity, boolean zeroItemsAllowed, 
			Random random) {
		try {
			String itemXpath = "id('cart_products')/tbody/tr[" + itemPosition + "]/td[2]/a";
			if(fDriver.findElement(By.xpath(itemXpath)).getText().equalsIgnoreCase(item.fName)) {
				setItemQuantity(itemPosition, maxItemQuantity, zeroItemsAllowed, random, item);
				return true;
			} else {
				return false;
			}

		} catch(NoSuchElementException e){
			throw (new Error("Product not found in the cart"));
		}
	}

	private void setItemQuantity(int itemPosition, int maxItemQuantity, boolean zeroItemsAllowed, Random random, Item item) {
		Integer quantity = calculateItemQuantity(maxItemQuantity, zeroItemsAllowed, random);
		String quantityInputXpath = "id('cart_products')/tbody/tr[" + itemPosition + "]/td[3]/form/input[@name='quantity']";
		WebElement quantityInputElement = fDriver.findElement(By.xpath(quantityInputXpath));
		
		quantityInputElement.clear();
		quantityInputElement.sendKeys(quantity.toString());
		quantityInputElement.sendKeys(Keys.ENTER);
		try{
			Thread.sleep(200);
		} catch(Exception e){
		}
		item.fQuantity = quantity;
	}

	private int calculateItemQuantity(int maxItemQuantity, boolean zeroItemsAllowed, Random random) {
		int quantity = 0;

		if(maxItemQuantity == 0){
			quantity = zeroItemsAllowed ? 0 : 1;
		} else{
			quantity = random.nextInt(maxItemQuantity) + (zeroItemsAllowed ? 0 : 1);
		}

		return quantity;
	}

	private BigDecimal calculateTotalPrice(LinkedHashSet<Item> items){
		int itemsCount = items.size(); 
		if (itemsCount == 0) {
			return new BigDecimal(0);
		}
		
		BigDecimal totalPrice = new BigDecimal(0);

		for(Item item : items) {
			BigDecimal itemPrice = evaluatePriceForItem(item, itemsCount); 
			totalPrice = totalPrice.add(itemPrice);
		}
		return totalPrice;
	}
	
	private BigDecimal evaluatePriceForItem(Item item, int itemsSize) {
		if (item.fQuantity == 0) {
			boolean isPresent = isElementPresent(By.xpath("id('cart_products')/tbody//a[contains(.,'" + item.fName + "')]"));
			Assert.assertFalse("Product shouldn't be present having quantity of 0", isPresent);
			return new BigDecimal(0);
		} 

		BigDecimal totalPrice = new BigDecimal(0);

		boolean found = false;

		for(int itemNumber = 1; itemNumber <= itemsSize; itemNumber++){
			try{
				if (itemHasThisName(itemNumber, item.fName)) {
					found = true;

					item.fPrice = getItemPrice(itemNumber);

					BigDecimal discount = getDiscount(itemNumber);
					if(discount != null){
						item.fDiscount = discount;
					}

					BigDecimal total = item.getTotalPrice();
					checkItemsTotalPrice(item, itemNumber, total);
					totalPrice = totalPrice.add(total);

					sleepMilliseconds(100);
					break;
				}
			} catch(NoSuchElementException e){
				throw(new Error("Product not found in the cart"));
			}
		}
		if(!found){
			throw(new Error("Product not found in the cart"));
		}
		
		return totalPrice;
	}

	private BigDecimal getItemPrice(int itemNumber) {
		return new BigDecimal(
				(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + itemNumber + "]/td[4]")).getText())
				.replace("$", ""));		
	}

	private boolean itemHasThisName(int itemNumber, String itemName){
		WebElement element = fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + itemNumber + "]/td[2]/a"));
		if(element.getText().equalsIgnoreCase(itemName)) {
			return true;
		} else {
			return false;
		}
	}

	private void sleepMilliseconds(int milliseconds) {
		try{
			Thread.sleep(milliseconds);
		} catch(Exception e){
		}
	}

	private BigDecimal getDiscount(int itemNumber) {
		BigDecimal discount = null;
		try{
			WebElement element = fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + itemNumber + "]/td[5]/span")); 
			discount = new BigDecimal(element.getText().replace("$", ""));
		} catch(NumberFormatException | NoSuchElementException e){
		}

		return discount;
	}

	private void checkItemsTotalPrice(Item item, int itemNumber, BigDecimal total) {
		Assert.assertTrue(
				"Price for " + item.fName + " doesn't match: " + item.fPrice.toString() + " times " + item.fQuantity.toString() + " == "
						+ total.toString() + ", found " + fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + itemNumber + "]/td[6]")).getText() + " !",
						total.toString().equals(
								(fDriver.findElement(By.xpath("id('cart_products')/tbody/tr[" + itemNumber + "]/td[6]")).getText())
								.replace("$", "")));

	}

}



