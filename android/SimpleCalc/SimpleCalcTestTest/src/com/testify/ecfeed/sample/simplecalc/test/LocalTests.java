package com.testify.ecfeed.sample.simplecalc.test;


public class LocalTests{

	public void testMethod(){
		System.out.println("testMethod()");
	}

	public void testMethodWith2Params(double arg0, MyType arg1){
		System.out.println("testMethodWith2Params(" + arg0 + ", " + arg1 + ")");
		
		String formattedValue = new com.testify.ecfeed.sample.simplecalc.ResultFormatter().getFormattedValue(arg0);
		
		System.out.println("Formatted value: " + formattedValue);
	}

	public void testMethodWith2Params(double arg0, MyType arg1, String result){
		// TODO Auto-generated method stub
		System.out.println("testMethodWith2Params(" + arg0 + ", " + arg1 + ", " + result + ")");
	}

}

