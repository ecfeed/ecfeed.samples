package workshop.runner.selenium.helpers;

import com.ecfeed.TestHandle;

import java.util.HashMap;

public class EcFeedHelper {

    private EcFeedHelper() {
    }

    public static void sendFeedbackPositive(TestHandle handle) {

        handle.addFeedback(true);
    }

    public static void sendFeedbackNegative(TestHandle handle, String status, String response) {
        var info = new HashMap<String, String>();

        info.put("Status", status);

        var responseElements = response.split("\n");

        for (var i = 0 ; i < responseElements.length ; i++) {
            info.put("" + (i+1), responseElements[i]);
        }

        handle.addFeedback(false, "Output error", info);
    }

    public static void sendFeedbackException(TestHandle handle) {

        handle.addFeedback(false);
    }
}
