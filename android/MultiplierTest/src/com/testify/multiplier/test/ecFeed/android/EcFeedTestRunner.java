package com.testify.multiplier.test.ecFeed.android;

import android.os.Bundle;
import com.testify.ecfeed.android.junit.tools.TestHelper;

public class EcFeedTestRunner extends android.test.InstrumentationTestRunner {

	@Override
	public void onCreate(Bundle arguments) {
	TestHelper.prepareTestArguments(arguments.getString("ecFeed"));
	super.onCreate(arguments);
	}
}
