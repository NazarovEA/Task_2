import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.LoginModel;
import models.RegisterModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {
    private UserSteps userSteps;
    private String token;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        userSteps = new UserSteps();
    }

    @Test
    public void loginWithUser() {
        RegisterModel registerModel = new RegisterModel();
        String email = "test_user_" + System.currentTimeMillis() + "@yandex.ru";
        registerModel.setEmail(email);
        registerModel.setPassword("A!!!ss14");
        registerModel.setName("Max");

        Response regResponse = userSteps.register(registerModel);
        token = regResponse.then().extract().path("accessToken");

        LoginModel loginModel = new LoginModel(email, "A!!!ss14");
        Response loginResponse = userSteps.login(loginModel);

        loginResponse.then()
                .assertThat()
                .body("accessToken", notNullValue())
                .body("success", is(true))
                .statusCode(200);
    }
    @AfterEach
    public void clearing(){
        if (token != null) {
            userSteps.delete(token);
 }
    }
}