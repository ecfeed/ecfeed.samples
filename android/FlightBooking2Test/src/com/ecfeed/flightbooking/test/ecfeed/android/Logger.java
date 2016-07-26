package com.ecfeed.flightbooking.test.ecfeed.android;

import android.util.Log;
import com.ecfeed.flightbooking.test.ecfeed.android.tools.ILogger;

public class Logger implements ILogger {

	final String TAG = "ecFeed";

	@Override
	public void log(String message) {
		Log.d(TAG, message);
	}
}
