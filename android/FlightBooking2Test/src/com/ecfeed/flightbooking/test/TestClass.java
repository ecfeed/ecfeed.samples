package com.ecfeed.flightbooking.test;


import com.ecfeed.flightbooking.MainActivity;
import com.ecfeed.flightbooking.test.ecfeed.android.EcFeedTest;

public class TestClass extends EcFeedTest {

	MainActivity fMainActivity;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		fMainActivity = getActivity();
	}

	public void testMethod() {
		// TODO Auto-generated method stub
		android.util.Log.d("ecFeed", "testMethod()");
		testMethodWithParams("1", "1", "1", "1", "1", "");
	}

	public void testMethodWithParams(String val1, String val2, String val3, String val4, 
			String expectedResult, String expectedComment){
		
		String airportFrom = "ATL";
		
		fMainActivity.setAirportFrom("ATL");
		sleep(2000);          
		
		assertEquals(airportFrom, fMainActivity.getAirportFrom());

//		final EditText result = (EditText) fMainActivity.findViewById(R.id.result);
//		final EditText comment = (EditText) fMainActivity.findViewById(R.id.comment);

//		String resultText = result.getText().toString(); 
//		String commentText = comment.getText().toString();
//
//		assertEquals("Actual comment differs from expected.", expectedComment, commentText);
//
//		if(expectedResult.equals("irrelevant") == false){
//			assertEquals("Actual result differs from expected.", expectedResult, resultText);
//		}
	}
	
	private void sleep(int milliseconds) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}