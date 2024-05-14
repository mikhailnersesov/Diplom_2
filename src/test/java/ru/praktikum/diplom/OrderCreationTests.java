package ru.praktikum.diplom;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.diplom.client.OrdersClient;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.OrderSteps;
import ru.praktikum.diplom.step.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreationTests   extends BaseTest  {
    protected static OrderSteps orderSteps;

    @Override
    @Before
    public void setUpMethod() {
        userSteps = new UserSteps(new UserClient());
        orderSteps = new OrderSteps(new OrdersClient());
        userSteps
                .createUserRequest(email, password, name);
    }



    public String getUserToken() {
        String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
        int spaceIndex = accessToken.indexOf(" ");
        return userToken = accessToken.substring(spaceIndex + 1);
    }

    @Test
    @DisplayName("Успешное создание заказа с авторизацией и c ингридиентами")
    @Description("Данный тест покрывает следующие кейсы: 1) можно создать заказ с валидными ингридиентами и без авторизации")
    public void createOrderWithIngedientsAuthorizedSucessfully() {
        userToken = getUserToken();

        ArrayList<String> ingredients = orderSteps.getIngredientsRequest().extract().path("data[0,1]._id");
        orderSteps.createOrdersRequest(ingredients, userToken).statusCode(SC_OK).body("success", is(true)).and().body("name", notNullValue());
    }

    @Test
    @DisplayName("Ошибка при создание заказа без авторизации c ингредиентами")
    @Description("Данный тест покрывает следующие кейсы: 1) можно создать заказ с валидными ингридиентами и без авторизации")
    public void createOrderWithIngedientsNotAuthorizedFailed() {
        ArrayList<String> ingredients = orderSteps.getIngredientsRequest().extract().path("data[0,1]._id");
        orderSteps.createOrdersRequest(ingredients).body("success", is(false)); //BUG: STEBURG-2: "success" state should be "false", but is "true"
    }

    @Test
    @DisplayName("Ошибка при создание заказа без авторизации и без ингредиентов")
    @Description("Данный тест покрывает следующие кейсы: 1) можно создать заказ с ингредиентами и без авторизации")
    public void createOrderWithoutIngedientsNotAuthorizedFailed400() {
        ArrayList<String> ingredients = new ArrayList<>();
        orderSteps.createOrdersRequest(ingredients).statusCode(SC_BAD_REQUEST).body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Ошибка при создание заказа c авторизацией, но без ингредиентов")
    @Description("Данный тест покрывает следующие кейсы: 1) можно создать заказ с ингридиентами и без авторизации")
    public void createOrderWithoutIngedientsAuthorizedFailed400() {
        userToken = getUserToken();

        ArrayList<String> ingredients = new ArrayList<>();
        orderSteps.createOrdersRequest(ingredients, userToken).statusCode(SC_BAD_REQUEST).body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Ошибка при создание заказа без авторизации и с невалидным хешем ингредиента")
    @Description("Данный тест покрывает следующие кейсы: 1) можно создать заказ с ингридиентами и без авторизации")
    public void createOrderWithInvalidIngredientNotAuthorizedFailed500() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("aaaa");
        ingredients.add("bbb");
        orderSteps.createOrdersRequest(ingredients).statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Ошибка при создание заказа с авторизацией, но с невалидным хешем ингредиента")
    @Description("Данный тест покрывает следующие кейсы: 1) можно создать заказ с ингридиентами и без авторизации")
    public void createOrderWithInvalidIngredientAuthorizedFailed500() {
        userToken = getUserToken();

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("aaaa");
        ingredients.add("bbb");
        orderSteps.createOrdersRequest(ingredients, userToken).statusCode(SC_INTERNAL_SERVER_ERROR);
    }


}
