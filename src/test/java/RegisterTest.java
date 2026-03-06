import io.restassured.RestAssured;
import models.RegisterModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class RegisterTest {
    private UserSteps userSteps;
    private String token;
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        userSteps = new UserSteps();
    }

    @Test
    public void createRegister() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("test_user_" + System.currentTimeMillis() + "@yandex.ru");
        registerModel.setPassword("P@ss123");
        registerModel.setName("Ivan");

        Response response = userSteps.register(registerModel);

        response.then()
                .assertThat()
                .body("success", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void fegisterNotEmail() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail(null);
        registerModel.setPassword("P@ss123");
        registerModel.setName("Ivan");

        Response response = userSteps.register(registerModel);

        response.then()
                .assertThat()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
    @AfterEach
    public void clearing() {
        if (token != null) {
            userSteps.delete(token)
                    .then()
                    .assertThat()
                    .statusCode(200);
        }
    }
}
