package com.ecfeed.multiplier.test;

import android.widget.EditText;

import com.ecfeed.multiplier.MainActivity;
import com.ecfeed.multiplier.R;
import com.ecfeed.multiplier.test.ecfeed.android.EcFeedTest;

public class TestClass extends EcFeedTest {

    MainActivity fMainActivity;

    @Override
    protected void setUp() throws Exception {
        fMainActivity = getActivity();
    }

    public void testMethod(String val1, String val2, String val3, String val4, 
                               String expectedResult, String expectedComment){
        try {
            sendKeys(val1 + " TAB");
            Thread.sleep(1000);
            sendKeys(val2 + " TAB");
            Thread.sleep(1000);
            sendKeys(val3 + " TAB");
            Thread.sleep(1000);
            sendKeys(val4 + " TAB");
            Thread.sleep(1000);

            sendKeys("ENTER");
            Thread.sleep(1000);          
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final EditText result = (EditText) fMainActivity.findViewById(R.id.result);
        final EditText comment = (EditText) fMainActivity.findViewById(R.id.comment);

        String resultText = result.getText().toString(); 
        String commentText = comment.getText().toString();

        assertEquals("Actual comment differs from expected.", expectedComment, commentText);

        if(expectedResult.equals("irrelevant") == false){
            assertEquals("Actual result differs from expected.", expectedResult, resultText);
        }
    }
}