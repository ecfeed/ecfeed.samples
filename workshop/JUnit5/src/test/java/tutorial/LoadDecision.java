package tutorial;

import com.ecfeed.TestProvider;
import com.ecfeed.type.TypeExport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class LoadDecision {

    private static final String model = "IMHL-K0DU-2U0I-J532-25J9";
    private static final String method = "com.example.test.LoanDecisionTest2.generateCustomerData";

    enum Gender {
        MALE, FEMALE
    }

    enum ID {
        PASSPORT, DRIVERS_LICENSE, PERSONAL_ID
    }

    static Iterable<Object[]> testProviderNWise() {
        return TestProvider.create(model).generateNWise(method);
    }

    @ParameterizedTest
    @MethodSource("testProviderNWise")
    void testProviderNWise(String name, String firstName, Gender gender, int age, String id, ID type) {
        System.out.println("name = " + name + ", firstName = " + firstName + ", gender = " + gender + ", age = " + age + ", id = " + id + ", type = " + type);
    }

    @Test
    @DisplayName("Export json")
    void exportTypeJson() {

        for (String chunk : TestProvider.create(model).exportNWise(method, TypeExport.JSON)) {
            System.out.println(chunk);
        }
    }

}
