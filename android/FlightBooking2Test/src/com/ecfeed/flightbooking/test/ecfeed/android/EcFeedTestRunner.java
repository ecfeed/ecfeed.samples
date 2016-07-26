package com.ecfeed.flightbooking.test.ecfeed.android;

import android.os.Bundle;
import com.ecfeed.flightbooking.test.ecfeed.android.tools.TestHelper;

public class EcFeedTestRunner extends android.test.InstrumentationTestRunner {

	@Override
	public void onCreate(Bundle arguments) {
	TestHelper.prepareTestArguments(arguments.getString("ecFeed"));
	super.onCreate(arguments);
	}
}
