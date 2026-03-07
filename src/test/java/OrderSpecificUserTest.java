import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.OrdersModel;
import models.RegisterModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class OrderSpecificUserTest {
    private UserSteps userSteps;
    private String token;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        userSteps = new UserSteps();
    }
    @Test
    public void OrderWithAuthorization(){
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("user_" + System.currentTimeMillis() + "@yandex.ru");
        registerModel.setPassword("RRREWR45");
        registerModel.setName("Vova");
        Response regResponse = userSteps.register(registerModel);
        token = regResponse.then().extract().path("accessToken");
//ингридиенты беру из постмана ручкой гет, в доке не верные хеш
        List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa6d");
        OrdersModel ordersModel = new OrdersModel(ingredients);
        Response response = userSteps.getOrderUser(token);

        //проверим созданный заказ
        response.then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
    }
    @Test
    public void OrderNotAuthorization(){
       Response response = userSteps.getOrderUser("");

        //проверим созданный заказ
        response.then()
                .log().all()
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}
