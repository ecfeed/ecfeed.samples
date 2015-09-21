package com.mamlambo.article.simplecalc.test.ecfeed;

import org.junit.Test;

import android.util.Log;
import android.widget.TextView;

import com.mamlambo.article.simplecalc.MainActivity;
import com.mamlambo.article.simplecalc.R;
import com.mamlambo.article.simplecalc.test.ecFeed.android.EcFeedTest;
//import com.testify.ecfeed.junit.SimpleRunner;

public class Ect1Test extends EcFeedTest {
	
	private MainActivity fMainActivity;
	private TextView fResult;
	
	private static final String TAG = "ecFeed";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

        fMainActivity = getActivity();
        fResult = (TextView) fMainActivity.findViewById(R.id.result);
	}

	@Test
	public void addValues(String keySequence1, String keySequence2) {
    	Log.d(TAG, "Ect1Test.addValues(" 
					+ keySequence1 + " ," 
					+ keySequence2 //+ " ,"
					+ //omitted +
					")");
    	
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
	
    String calculateResultForFixedCase(String keySequence1, String keySequence2) {
    	
    	String seq1 = keySequence1.trim();
    	String seq2 = keySequence2.trim();
    	
    	if (seq1.equals("2 4 ENTER") && seq2.equals("7 4 ENTER")) {
    		return "11"; // error result
    	}
    	
    	return null;
    }

    public void ecFeedIntegration() {

        final String NUMBER_24 = "2 4 ENTER ";
        final String NUMBER_74 = "7 4 ENTER ";
    	
        Log.d(TAG, "function: testUserAddValues00 beg");
    	addValues(NUMBER_24, NUMBER_74);
    	Log.d(TAG, "function: testUserAddValues00 end");
    }
    
    public void testUserAddValues01() {
    	
        final String NUMBER_24 = "2 4 ENTER ";
        final String NUMBER_74 = "7 4 ENTER ";
        final String ADD_RESULT = "98";

    	Log.d(TAG, "function: testUserAddValues01 beg");
    	
        // we use sendKeys instead of setText so it goes through entry
        // validation
        sendKeys(NUMBER_24);
        // now on value2 entry
        sendKeys(NUMBER_74);

        // now on Add button
        sendKeys("ENTER");

        // get fResult
        String mathResult = fResult.getText().toString();
        assertTrue("Add fResult should be |98| " + ADD_RESULT + " but was |" + mathResult + "|", 
        			mathResult.equals(ADD_RESULT));
        
//        Log.d(TAG, "function: addValues end");
    }

    public void testUserAddValues02() {
    	
        final String NUMBER_24 = "2 4 ENTER ";
        final String NUMBER_74 = "7 4 ENTER ";
        final String ADD_RESULT = "98";

    	Log.d(TAG, "function: testUserAddValues02 beg");
    	
        // we use sendKeys instead of setText so it goes through entry
        // validation
        sendKeys(NUMBER_24);
        // now on value2 entry
        sendKeys(NUMBER_74);

        // now on Add button
        sendKeys("ENTER");

        // get fResult
        String mathResult = fResult.getText().toString();
        assertTrue("Add fResult should be |98| " + ADD_RESULT + " but was |" + mathResult + "|", 
        			mathResult.equals(ADD_RESULT));
        
//        Log.d(TAG, "function: addValues end");
    }

	public void testMethodFFF(){
		android.util.Log.d("ecFeed", "testMethodFFF()");
		
//		String myParamClass = MyType.class.toString();
//		android.util.Log.d("ecFeed", "myParamClass: <" + myParamClass + ">");
//		
//		String myParamClassCorrected = "com.mamlambo.article.simplecalc.test.ecfeed.MyType";
//		android.util.Log.d("ecFeed", "myParamClassCorrected: <" + myParamClassCorrected + ">");
//		
//		if (myParamClass.equals(myParamClassCorrected)) {
//			android.util.Log.d("ecFeed", "EQUAL ---------");
//		}
//		
//		Class<?> cl = null;
//		try {
//			cl = Class.forName(myParamClassCorrected);
//		} catch (ClassNotFoundException e) {
//			android.util.Log.d("ecFeed", "EXCEPTION ");
//			return;
//		}
//		
//		android.util.Log.d("ecFeed", "AFTER TRY CATCH");
	}

	public void testUserParam(MyType myParam1){
		android.util.Log.d("ecFeed", "testUserParam(" + myParam1 + ")");
		
		
	}

	public void testUserParam(MyType arg0, MyType arg1){
		android.util.Log.d("ecFeed", "testUserParam(" + arg0 + ", " + arg1 + ")");
	}

}

