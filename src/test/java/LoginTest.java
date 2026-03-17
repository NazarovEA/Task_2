import io.restassured.response.Response;
import models.LoginModel;
import models.RegisterModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseTest {
    private UserSteps userSteps;
    private String token;

    @BeforeEach
    public void setUp() {
      //  RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("логин под существующим пользователем")
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


    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithWrongPasswordAndEmail() {

        String wrongEmail = "non_existent_user_" + System.currentTimeMillis() + "@yandex.ru";
        // Сначала регистрируем пользователя с правильным паролем
        RegisterModel registerModel = new RegisterModel(wrongEmail, "CorrectPass123", "Max");
        Response regResponse = userSteps.register(registerModel);
        token = regResponse.then().extract().path("accessToken");

        // заходим с неверным паролем и почтой
        LoginModel loginModel = new LoginModel(wrongEmail, "WrongPass777");
        Response loginResponse = userSteps.login(loginModel);

                loginResponse.then()
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
}
}