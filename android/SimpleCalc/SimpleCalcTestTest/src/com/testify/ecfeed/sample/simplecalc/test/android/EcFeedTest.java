package com.testify.ecfeed.sample.simplecalc.test.android;

import android.test.ActivityInstrumentationTestCase2;

import com.testify.ecfeed.android.junit.tools.TestHelper;
import com.testify.ecfeed.sample.simplecalc.MainActivity;

public class EcFeedTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public EcFeedTest() {
		super(MainActivity.class);
	}

	public void ecFeedTest() {
		TestHelper.invokeTestMethod(this, new Logger());
	}
}
