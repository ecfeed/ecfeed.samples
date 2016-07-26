package com.ecfeed.flightbooking.test.ecfeed.android;

import android.test.ActivityInstrumentationTestCase2;
import com.ecfeed.flightbooking.test.ecfeed.android.tools.TestHelper;

public class EcFeedTest extends ActivityInstrumentationTestCase2<com.ecfeed.flightbooking.MainActivity> {

	public EcFeedTest() {
		super(com.ecfeed.flightbooking.MainActivity.class);
	}

	public void ecFeedTest() {
		TestHelper.invokeTestMethod(this, new Logger());
	}
}
