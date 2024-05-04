package ru.praktikum.diplom.step;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.json.JSONArray;
import ru.praktikum.diplom.client.OrdersClient;
import ru.praktikum.diplom.dto.OrdersIngredientsRequest;

import java.util.ArrayList;

public class OrderSteps {
    private final OrdersClient ordersClient;

    public OrderSteps(OrdersClient ordersClient) {
        this.ordersClient = ordersClient;
    }

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getIngredientsRequest() {
        return ordersClient.sendGetRequestIngredients().then();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrdersRequest(ArrayList<String> ingredients) {
        return createOrdersRequest(ingredients, "");
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrdersRequest(ArrayList<String> ingredients,String userToken) {
        OrdersIngredientsRequest ordersIngredientsRequest = new OrdersIngredientsRequest();
        ordersIngredientsRequest.setIngredients(ingredients);
        return ordersClient.sendPostRequestOrdersCreate(ordersIngredientsRequest,userToken).then();
    }
}
