package pages.elements.patterns;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class FormElement extends AbstractWebElementContainer {
	protected final WebDriver fDriver;

	
	public FormElement(WebDriver driver, WebElement element){
		super(element);
		initialiseElements();
		this.fDriver = driver;
	}
	
	/*
	 * add submit logic here
	 */
	public abstract void submit();
	
	public boolean fillTextField(String fieldName, String keys){
		EditableTextElement element = (EditableTextElement)getElement(fieldName, AbstractWebElementTypes.EDITABLE_TEXT);
		if(element != null){
			element.clearAndType(keys);
			return true;
		}
		return false;		
	}
	
	public boolean elementHasError(String element_name){
		AbstractWebElement element = fElements.get(element_name);
		if(element != null){
			return element.hasError();
		} else {
			throw new Error("Element you are seeking error within not found");
		}
	}
	
	public boolean formHasError(){
		for(String key: fElements.keySet()){
			if(fElements.get(key).hasError()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isVisible(){
		return fElement.isDisplayed();		
	}
	
	protected abstract void initialiseElements();	
	
}
