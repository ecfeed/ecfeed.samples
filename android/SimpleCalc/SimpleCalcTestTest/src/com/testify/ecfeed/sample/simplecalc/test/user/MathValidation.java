package com.testify.ecfeed.sample.simplecalc.test.user;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.mamlambo.article.simplecalc.R;
//import com.testify.ecfeed.junit.SimpleRunner;
import com.testify.ecfeed.sample.simplecalc.MainActivity;

//@RunWith(SimpleRunner.class)
//@RunWith(OnlineRunner.class)
//@EcModel("src/Tutorial1.ect")
//@Generator(CartesianProductGenerator.class)
public class MathValidation extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity fMainActivity;
	private TextView fResult;
	private static final String TAG = "ecFeed";
	
	public MathValidation() {
		super(MainActivity.class);
	}	

	@Override
	protected void setUp() throws Exception {
		super.setUp();

        fMainActivity = getActivity();
        fResult = (TextView) fMainActivity.findViewById(R.id.result);
	}
	
    private static final String NUMBER_24 = "2 4 ENTER ";
    private static final String NUMBER_74 = "7 4 ENTER ";
//    private static final String NUMBER_5_DOT_5 = "5 PERIOD 5 ENTER ";

    private static final String ADD_RESULT = "98";
//    private static final String ADD_DECIMAL_RESULT = "79.5";
//    private static final String MULTIPLY_RESULT = "1776";

    public void testFunWithParameter(int arg) {
    }
    
    public void testAddValues() {
    	
    	Log.d(TAG, "function: addValues beg");
    	
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
        
        Log.d(TAG, "function: addValues end");
    }

//    public void testAddDecimalValues() {
//    	
//    	Log.d(TAG, "function: testAddDecimalValues beg");
//    	
//        sendKeys(NUMBER_5_DOT_5 + NUMBER_74 + "ENTER");
//
//        String mathResult = fResult.getText().toString();
//        assertTrue("Add fResult should be |" + ADD_DECIMAL_RESULT + "| but was |" + mathResult + "|", 
//        		mathResult.equals(ADD_DECIMAL_RESULT));
//        
//        Log.d(TAG, "function: testAddDecimalValues end");
//    }
//
//    public void testMultiplyValues() {
//    	
//    	Log.d(TAG, "function: testMultiplyValues beg");
//    	
//    	sendKeys();
//        sendKeys(NUMBER_24 + NUMBER_74 + " DPAD_RIGHT ENTER");
//
//        String mathResult = fResult.getText().toString();
//        assertTrue("Multiply fResult should be |" + MULTIPLY_RESULT + "| but was " + mathResult + "|", 
//        		mathResult.equals(MULTIPLY_RESULT));
//        
//        Log.d(TAG, "function: testMultiplyValues end");
//    }    
//    
//    private void checkFormat(double value, String formattedValue) {
//    	assertEquals(formattedValue, fMainActivity.getResultText(value));
//    }
//    
//    @Test
//    public void testResultFormatting() {
//    	
//    	Log.d(TAG, "function: testResultFormatting beg");
//    	checkFormat(33.0, "33");
//    	checkFormat(33.5, "33.5");
//    	checkFormat(-1.5, "-1.5");
//    	checkFormat(-2.0, "-2");
//    	Log.d(TAG, "function: testResultFormatting end");
//    }
//
////    @Test
////	public void testMethodEct1(int arg0){
////		// TODO Auto-generated method stub
////		System.out.println("testMethodEct1(" + arg0 + ")");
////	}
////
////    @Test
////	public void testMethodEct2(){
////		// TODO Auto-generated method stub
////		System.out.println("testMethodEct2()");
////	}
}