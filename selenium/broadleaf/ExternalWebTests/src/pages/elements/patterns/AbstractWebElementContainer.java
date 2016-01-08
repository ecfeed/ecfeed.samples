package pages.elements.patterns;

import java.util.HashMap;

import org.openqa.selenium.WebElement;

import pages.patterns.SearchContextElementLocatorFactory;

public abstract class AbstractWebElementContainer extends AbstractWebElement{

	protected final HashMap<String, AbstractWebElement> fElements;
	protected final SearchContextElementLocatorFactory fElementLocatorFactory;

	public AbstractWebElementContainer(WebElement element){
		super(element);
		fElementLocatorFactory = new SearchContextElementLocatorFactory(fElement);
		fElements = new HashMap<String, AbstractWebElement>();
	}

	public AbstractWebElement getChildElement(String fieldName){
		AbstractWebElement element = fElements.get(fieldName);
		if(element != null){
			return element;
		} else{
			throw new Error("Invalid element: " + fieldName);
		}
	}

	public AbstractWebElement getElement(String name, AbstractWebElementTypes type){
		AbstractWebElement element = fElements.get(name);
		switch(type){
		case BUTTON:
			if(element instanceof ButtonElement)
				return element;
			break;
		case COMBO:
			if(element instanceof ComboElement)
				return element;
			break;
		case FORM:
			if(element instanceof FormElement)
				return element;
			break;
		case MENU:
			if(element instanceof MenuElement)
				return element;
			break;
		case RADIO:
			if(element instanceof RadioElement)
				return element;
			break;
		case EDITABLE_TEXT:
			if(element instanceof EditableTextElement)
				return element;
			break;
		case TEXT:
			if(element instanceof TextElement)
				return element;
			break;
		default:
			break;
		}
		return null;
	}

	protected abstract void initialiseElements();

}
