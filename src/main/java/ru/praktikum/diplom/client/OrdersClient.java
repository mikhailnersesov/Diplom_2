package ru.praktikum.diplom.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom.dto.OrdersIngredientsRequest;

public class OrdersClient extends RestClient {

    @Step("Send POST request to /orders")
    public Response sendPostRequestOrdersCreate(OrdersIngredientsRequest ordersIngredientsRequest, String accessToken) {
        return getdefaultRequestSpecification().auth().oauth2(accessToken)
                .body(ordersIngredientsRequest)
                .when()
                .post("/orders");
    }
    @Step("Send GET request to /orders")
    public Response sendGetRequestOrders(String accessToken) {
        return getdefaultRequestSpecification().auth().oauth2(accessToken)
                .when()
                .get("/orders");
    }

    @Step("Send GET request to /ingredients")
    public Response sendGetRequestIngredients() {
        return getdefaultRequestSpecification()
                .when()
                .get("/ingredients");
    }
}
