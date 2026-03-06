import models.LoginModel;
import models.RegisterModel;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserSteps {
    @Step("Регистрация")
    public Response register(RegisterModel model) {
        return given()
                .log().all()
        .header("Content-type", "application/json")
        .body(model)
        .when()
        .post("/api/auth/register");
    }

    @Step("Удаление")
    public Response delete(String token) {
        return given()
                .log().all()
        .header("Authorization", token)
        .when()
        .delete("/api/auth/user");
    }

    @Step ("Логин")
    public Response login(LoginModel model) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(model)
                .when()
                .post("/api/auth/login");
    }


}

