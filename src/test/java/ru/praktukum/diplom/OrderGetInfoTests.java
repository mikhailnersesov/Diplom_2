package ru.praktukum.diplom;

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

public class OrderGetInfoTests {
    protected static List<String> userTokens = new ArrayList();
    protected String userToken;
    protected static UserSteps userSteps;
    protected static OrderSteps orderSteps;
    private final String email = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
    private final String password = RandomStringUtils.randomAlphabetic(10);
    private final String name = RandomStringUtils.randomAlphabetic(10);

    @AfterClass
    public static void tearDown() {
        for (int i = 0; i < userTokens.size(); i++) {
            if (userTokens.get(i) != null) {
                userSteps.deleteUserRequest(userTokens.get(i)).statusCode(SC_ACCEPTED).body("message", is("User successfully removed"));
            }
        }
    }

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        orderSteps = new OrderSteps(new OrdersClient());
        userSteps
                .createUserRequest(email, password, name);
    }

    @After
    public void getUserIdIfWasSuccessfullyCreated() {
        try {
            String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
            int spaceIndex = accessToken.indexOf(" "); // Find the index of the space character
            userToken = accessToken.substring(spaceIndex + 1);  // Extract the second part of the string using substring
        } catch (AssertionError assertionError) {
            System.out.println("no users was created - nothing to save");
        }
        userTokens.add(userToken);
    }

    public String getUserToken() {
        String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
        int spaceIndex = accessToken.indexOf(" ");
        return userToken = accessToken.substring(spaceIndex + 1);
    }
    public void createOrderWithIngedientsAuthorizedSucessfully(){
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
        userToken = getUserToken();
        orderSteps.getOrdersRequest().statusCode(SC_UNAUTHORIZED).body("success", is(false)).and().body("message", is("You should be authorised"));
    }


}
