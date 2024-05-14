package ru.praktikum.diplom.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom.dto.OrdersIngredientsRequest;

import static ru.praktikum.diplom.config.OrdersConfig.INGREDIENTS_ENDPOINT;
import static ru.praktikum.diplom.config.OrdersConfig.ORDERS_ENDPOINT;

public class OrdersClient extends RestClient {

    @Step("Send POST request to /orders")
    public Response sendPostRequestOrdersCreate(OrdersIngredientsRequest ordersIngredientsRequest, String accessToken) {
        return getDefaultRequestSpecification().auth().oauth2(accessToken)
                .body(ordersIngredientsRequest)
                .when()
                .post(ORDERS_ENDPOINT);
    }

    @Step("Send GET request to /orders")
    public Response sendGetRequestOrders(String accessToken) {
        return getDefaultRequestSpecification().auth().oauth2(accessToken)
                .when()
                .get(ORDERS_ENDPOINT);
    }

    @Step("Send GET request to /ingredients")
    public Response sendGetRequestIngredients() {
        return getDefaultRequestSpecification()
                .when()
                .get(INGREDIENTS_ENDPOINT);
    }
}
