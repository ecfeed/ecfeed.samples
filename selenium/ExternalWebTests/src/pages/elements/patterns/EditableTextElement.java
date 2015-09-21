package pages.elements.patterns;

import org.openqa.selenium.WebElement;

public class EditableTextElement extends TextElement {
	
	public EditableTextElement(WebElement element){
		super(element);
	}
	
	public void clearAndType(String text){
		fElement.clear();
		fElement.sendKeys(text);
	}
	
	public void type(String text){
		fElement.sendKeys(text);
	}
	
	public boolean isEnabled(){
		return fElement.isEnabled();
	}

}
