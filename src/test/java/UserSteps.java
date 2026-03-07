import models.LoginModel;
import models.OrdersModel;
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
@Step("Создание заказа")
public Response NewOrder(OrdersModel order, String token){
    return given()

            .log().all()
            .header("Content-type", "application/json")
            .header("Authorization", token)
            .body(order)
            .when()
            .post("/api/orders");
}

//метод принимающий токен
    @Step
    public Response getOrderUser(String token){

        return given()

                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", token == null ? "" : token)
                .when()
                .get("/api/orders");
    }
}

