package pages.elements.patterns;

import org.openqa.selenium.WebElement;

public abstract class AbstractWebElement{
	protected WebElement fElement;
	
	public AbstractWebElement(WebElement element){
		fElement = element;
	}
	
	public WebElement getRawElement(){
		return fElement;
	}
	
	public boolean hasError(){
		if(fElement != null){
			String fieldClass = fElement.getAttribute("class");
			return(fieldClass.contains("fieldError"));
		}
		return false;
	}

}
