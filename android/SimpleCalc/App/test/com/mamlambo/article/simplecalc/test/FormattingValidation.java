package com.mamlambo.article.simplecalc.test;

import org.junit.Test;

//import com.testify.ecfeed.generators.CartesianProductGenerator;
//import com.testify.ecfeed.junit.OnlineRunner;
//import com.testify.ecfeed.junit.annotations.EcModel;
//import com.testify.ecfeed.junit.annotations.Generator;

import com.testify.ecfeed.sample.simplecalc.ResultFormatter;

import junit.framework.TestCase;

//@RunWith(OnlineRunner.class)
//@EcModel("SimpleCalcEct.ect")
//@Generator(CartesianProductGenerator.class)
public class FormattingValidation extends TestCase {

	ResultFormatter resultFormatter = new ResultFormatter();
	
	public FormattingValidation() {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	private String getExpectedFormattedValue(double value) {
		if(value == 33.0) {
			return "33";
		}
		
		if(value == 33.5) {
			return "33.5";
		}
		
		if (value == -1.0) {
			return "-1";
		}		
		
		if (value == -1.5) {
			return "-1.5";
		}
		
		if (value == 7.0) {
			return "7";
		}
			
		if (value == 7.5) {
			return "7.5";
		}
		
		return new String();
	}

	@Test
    public void testFormat(double value) {
		System.out.println("testFormat(" + value + ")");
    	assertEquals(getExpectedFormattedValue(value), resultFormatter.getFormattedValue(value));
    }
    
	@Test
    public void testResultFormatting() {
		System.out.println("testResultFormatting BEG");
    	testFormat(7);
    	testFormat(7.5);
    	System.out.println("testResultFormatting END");
    }

	@Test
	public void testMethod1(){
		// TODO Auto-generated method stub
		System.out.println("testMethod1()");
	}
}

