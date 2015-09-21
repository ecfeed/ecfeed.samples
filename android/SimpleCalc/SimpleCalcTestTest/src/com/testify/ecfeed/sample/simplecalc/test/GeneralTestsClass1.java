package com.testify.ecfeed.sample.simplecalc.test;

import android.util.Log;
import android.widget.TextView;

import com.mamlambo.article.simplecalc.R;
import com.testify.ecfeed.sample.simplecalc.MainActivity;
import com.testify.ecfeed.sample.simplecalc.test.android.EcFeedTest;

public class GeneralTestsClass1 extends EcFeedTest {
	
	private MainActivity fMainActivity;
	private TextView fResult;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

        fMainActivity = getActivity();
        fResult = (TextView) fMainActivity.findViewById(R.id.result);
	}

	public void testMethod1(){
		Log.d("ecFeed", "testMethod1()");
	}

	public void addValues(String keySequence1, String keySequence2){
		Log.d("ecFeed", "addValues(" + keySequence1 + ", " + keySequence2 + ")");
		
        sendKeys(keySequence1);
        sendKeys(keySequence2);
        sendKeys("ENTER");

        String result = fResult.getText().toString();
        String expectedResult = calculateExpectedResult(keySequence1, keySequence2);

        assertTrue("Add result should be <" + expectedResult + "> but was <" + result + ">", 
        		result.equals(expectedResult));
	}
	
    private String calculateExpectedResult(String keySequence1, String keySequence2) {
    	
//    	String fixedResStr = calculateResultForFixedCase(keySequence1, keySequence2);
//    	
//    	if (fixedResStr != null) {
//    		return fixedResStr;
//    	}
    	
    	int par1 = keySequenceToInt(keySequence1);
    	int par2 = keySequenceToInt(keySequence2);
    	
    	Integer result = par1 + par2;
    	String resultStr = result.toString();
    	
    	return resultStr;
    }	
    
    private int keySequenceToInt(String keySequence) {
    	Integer i1 = Integer.decode(String.valueOf(keySequence.charAt(0)));
    	Integer i2 = Integer.decode(String.valueOf(keySequence.charAt(2)));
    	
    	return 10*i1 + i2;
    }

	public void addValuesWithExpectedResult(String keySequence1, String keySequence2, String expectedResult){
		Log.d("ecFeed", "addValuesWithExpectedResult(" + keySequence1 + ", " + keySequence2 + ", " + expectedResult + ")");
		
        sendKeys(keySequence1);
        sendKeys(keySequence2);
        sendKeys("ENTER");

        String result = fResult.getText().toString();

        assertTrue("Add result should be <" + expectedResult + "> but was <" + result + ">", 
        		result.equals(expectedResult));
	}

	public void functionToImport(String keySequence1, String keySequence2){
		Log.d("ecFeed", "functionToImport(" + keySequence1 + ", " + keySequence2 + ")");
	}

	public void nonInterfaceAdd(int param1, int param2){
		Log.d("ecFeed", "nonInterfaceAdd(" + param1 + ", " + param2 + ")");
	}

	public void paramsTest(boolean arg0, MyType arg1){
		Log.d("ecFeed", "paramsTest(" + arg0 + ", " + arg1 + ")");
	}

	public void testMethod3(int arg0){
		// TODO Auto-generated method stub
		System.out.println("testMethod3(" + arg0 + ")");
	}

}

