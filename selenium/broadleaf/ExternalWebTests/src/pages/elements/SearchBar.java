package pages.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import pages.elements.patterns.ButtonElement;
import pages.elements.patterns.EditableTextElement;
import pages.elements.patterns.FormElement;

public class SearchBar extends FormElement{
	
	public final static String SEARCH_TEXT = "search";
	public final static String SEARCH_BUTTON = "search_button";
	
	@FindBy(how = How.CLASS_NAME, using = SEARCH_TEXT)
	private WebElement fSearchText;
	@FindBy(how = How.ID, using = SEARCH_BUTTON)
	private WebElement fSearchButton;

	public SearchBar(WebElement element, WebDriver driver){
		super(driver, element);
	}

	@Override
	public void submit(){
		((ButtonElement)fElements.get(SEARCH_BUTTON)).click();
	}
	
	public void fillSearch(String words){
		((EditableTextElement)fElements.get(SEARCH_BUTTON)).type(words);
		
	}

	@Override
	protected void initialiseElements(){
		fElements.put(SEARCH_TEXT, new EditableTextElement(fSearchText));
		fElements.put(SEARCH_BUTTON, new ButtonElement(fSearchButton));
		PageFactory.initElements(fElementLocatorFactory, this);
	}

}
