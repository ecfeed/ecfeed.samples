package com.mamlambo.article.simplecalc.test.ecFeed.android;

import android.test.ActivityInstrumentationTestCase2;
import com.testify.ecfeed.android.junit.tools.TestHelper;
import com.mamlambo.article.simplecalc.MainActivity;

public class EcFeedTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public EcFeedTest() {
		super(MainActivity.class);
	}

	public void ecFeedTest() {
		TestHelper.invokeTestMethod(this, new Logger());
	}
}
