package workshop.runner.playwright.helpers;

import com.ecfeed.TestHandle;

import java.util.HashMap;

public class EcFeedHelper {

    private EcFeedHelper() {
    }

    public static String sendFeedbackPositive(TestHandle handle) {

        handle.addFeedback(true, "Success");

        return "Success";
    }

    public static String sendFeedbackNegative(TestHandle handle, String status, String response) {
        var info = new HashMap<String, String>();

        info.put("Status", status);

        var responseElements = response.split("\n");

        for (var i = 0 ; i < responseElements.length ; i++) {
            info.put("" + (i+1), responseElements[i]);
        }

        handle.addFeedback(false, "Failure - Output error", info);

        return "Failure";
    }

    public static String sendFeedbackException(TestHandle handle, Throwable e) {

        handle.addFeedback(false, "Failure - Input error");

        return "Exception";
    }
}
