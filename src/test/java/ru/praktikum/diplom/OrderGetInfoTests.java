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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class OrderGetInfoTests    extends BaseTest {
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

    public void createOrderWithIngedientsAuthorizedSucessfully() {
        userToken = getUserToken();

        ArrayList<String> ingredients = orderSteps.getIngredientsRequest().extract().path("data[0,1]._id");
        orderSteps.createOrdersRequest(ingredients, userToken).statusCode(SC_OK).body("success", is(true)).and().body("name", is("Бессмертный флюоресцентный бургер"));
    }

    @Test
    @DisplayName("Получение заказов (1 заказ) конкретного пользователя, c авторизацией")
    @Description("Данный тест покрывает следующие кейсы: 1) можно получить список заказов конкретного пользователя (если он сделал только один заказ), предварительно авторизовавшись")
    public void getOneOrdersAuthorizedSucessfully() {
        createOrderWithIngedientsAuthorizedSucessfully();
        userToken = getUserToken();
        orderSteps.getOrdersRequest(userToken).statusCode(SC_OK).body("success", is(true)).and().body("orders[0].status", is("done"));
    }

    @Test
    @DisplayName("Получение заказов (несколько заказов) конкретного пользователя, c авторизацией")
    @Description("Данный тест покрывает следующий кейс: 1) можно получить список заказов конкретного пользователя (если он сделал несколько заказов), предварительно авторизовавшись")
    public void getManyOrdersAuthorizedSucessfully() {
        createOrderWithIngedientsAuthorizedSucessfully();
        createOrderWithIngedientsAuthorizedSucessfully();
        userToken = getUserToken();
        orderSteps.getOrdersRequest(userToken).statusCode(SC_OK).body("success", is(true)).and().body("orders[0].status", is("done")).and().body("orders[1].status", is("done"));
    }

    @Test
    @DisplayName("Получение заказов (ни одного заказа) конкретного пользователя, c авторизацией")
    @Description("Данный тест покрывает следующие кейсы: 1) можно получить список заказов конкретного пользователя (если он сделал не сделал ни одного заказа), предварительно авторизовавшись")
    public void getNullOrdersAuthorizedSucessfully() {
        userToken = getUserToken();
        orderSteps.getOrdersRequest(userToken).statusCode(SC_OK).body("success", is(true)).and().body("orders", empty());
    }

    @Test
    @DisplayName("Ошибка при получении заказов (1 заказ) конкретного пользователя, без авторизацией")
    @Description("Данный тест покрывает следующие кейсы: 1) нельзя получить список заказов конкретного пользователя (если он сделал только один заказ), не авторизовавшись")
    public void getOneOrdersNotAuthorizedFailed401() {
        createOrderWithIngedientsAuthorizedSucessfully();
        userToken = "";
        orderSteps.getOrdersRequest(userToken).statusCode(SC_UNAUTHORIZED).body("success", is(false)).and().body("message", is("You should be authorised"));
    }


}
