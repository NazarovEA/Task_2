import models.RegisterModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class RegisterTest extends BaseTest {
    private UserSteps userSteps;
    private String token;

    @BeforeEach
    public void setUp() {

        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Создание пользователя")
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
    @DisplayName("Создание пользователя без почты")
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

    @Test
    @DisplayName("Создание пользователя,уже зарегистрированного")
    public void registerExistingUser() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("replay_user_" + System.currentTimeMillis() + "@ya.ru");
        registerModel.setPassword("replay88");
        registerModel.setName("Vova");

        Response oneResponse = userSteps.register(registerModel);
        // Извлечь токен
        token = oneResponse.then().extract().path("accessToken");
        oneResponse.then().statusCode(200);
//после регистрации регистрируем второй раз
        Response doubleResponse = userSteps.register(registerModel);

                doubleResponse.then()
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @AfterEach
    public void clearing() {
        if (token != null) {
            userSteps.delete(token)
                    .then()
                    .assertThat()
                    .statusCode(202);
        }
    }
}
