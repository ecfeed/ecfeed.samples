package com.mamlambo.article.simplecalc.test.ecFeed.android;

import android.util.Log;
import com.testify.ecfeed.android.junit.tools.ILogger;

public class Logger implements ILogger {

	final String TAG = "ecFeed";

	@Override
	public void log(String message) {
		Log.d(TAG, message);
	}
}
