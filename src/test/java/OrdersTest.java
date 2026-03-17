import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.OrdersModel;
import models.RegisterModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrdersTest extends BaseTest {
    private UserSteps userSteps;
    private String token;

    @BeforeEach
    public void setUp() {
       // RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        userSteps = new UserSteps();
    }
// Заказ полноценный
    @Test
    @DisplayName("Создание заказа с регистрацией и ингредиентами")
    public void newOrder() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("user_" + System.currentTimeMillis() + "@yandex.ru");
        registerModel.setPassword("RRREWR45");
        registerModel.setName("Vova");
        Response regResponse = userSteps.register(registerModel);
        token = regResponse.then().extract().path("accessToken");
//ингридиенты беру из постмана ручкой гет, в доке не верные хеш
        //List<String> ingredients = List.of("61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa6d");
        Response ingredientsResponse = userSteps.getIngredients();
        List<String> ingredients = ingredientsResponse.then()
                .extract()
                .path("data._id");
        List<String> selectedIngredients = List.of(ingredients.get(0), ingredients.get(1));
        OrdersModel ordersModel = new OrdersModel(ingredients);
        Response response = userSteps.NewOrder(ordersModel,   token);

                //проверим созданный заказ
        response.then()
                .assertThat()
                .statusCode(200)
                .body("order.number", notNullValue())
                .body("success", is(true));
    }

//Заказ без ингридиентов
    @Test
    @DisplayName("Создание заказа с регистрацией без ингредиентов")
    public void notIngridientsOrder() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("user_" + System.currentTimeMillis() + "@yandex.ru");
        registerModel.setPassword("RRREWR45");
        registerModel.setName("Vova");
        Response regResponse = userSteps.register(registerModel);
        token = regResponse.then().extract().path("accessToken");
//ингридиенты беру из постмана ручкой гет, в доке не верные хеш
        List<String> ingredients = List.of();
        OrdersModel ordersModel = new OrdersModel(ingredients);
        Response response = userSteps.NewOrder(ordersModel,   token);

        //проверим созданный заказ
        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"))
                .body("success", is(false));
    }

    //Заказ с невалидными ингридиентами
    @Test
    @DisplayName("Создание заказа с не валидными ингредиентами")
    public void notValidIngridients() {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setEmail("user_" + System.currentTimeMillis() + "@yandex.ru");
        registerModel.setPassword("RRREWR45");
        registerModel.setName("Vova");
        Response regResponse = userSteps.register(registerModel);
        token = regResponse.then().extract().path("accessToken");
//невалидные ингридиенты
        List<String> ingredients = List.of("1","@","#");
        OrdersModel ordersModel = new OrdersModel(ingredients);
        Response response = userSteps.NewOrder(ordersModel,   token);

        //проверим созданный заказ
        response.then()
                .assertThat()
                .statusCode(500);
    }
    @AfterEach
    public void clearing(){
        if (token != null) {
            userSteps.delete(token);
        }
    }
}

