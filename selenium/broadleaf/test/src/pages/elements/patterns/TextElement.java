package pages.elements.patterns;

import org.openqa.selenium.WebElement;

public class TextElement extends AbstractWebElement {

	public TextElement(WebElement element){
		super(element);
	}
	
	public String getValue(){
		return fElement.getAttribute("value");
	}
	
	public String getText(){
		return fElement.getText();
	}

}
