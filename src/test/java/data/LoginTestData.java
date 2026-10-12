package data;

import org.testng.annotations.DataProvider;

public class LoginTestData {

    @DataProvider(
            name = "validLoginData"
    )
    public static Object[][] validLoginData() {

        return new Object[][]{
                {"standard_user", "secret_sauce"}
        };
    }

    @DataProvider(
            name = "invalidLoginData"
    )
    public static Object[][] invalidLoginData() {

        return new Object[][]{
                {"standard_user", "wrong_password"},
                {"wrong_user", "secret_sauce"},
                {"wrong_user", "wrong_password"}
        };
    }
}