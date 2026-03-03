import models.RegisterModel;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserSteps {
    @Step("Регистрация")
    public Response register(RegisterModel model) {
        return given()
        .header("Content-type", "application/json")
        .body(model)
        .when()
        .post("/api/auth/register");
    }

    @Step("Удаление")
    public Response delete(String token) {
        return given()
        .header("Authorization", token)
        .when()
        .delete("/api/auth/user");
    }
}

