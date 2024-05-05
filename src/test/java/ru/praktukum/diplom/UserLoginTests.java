package ru.praktukum.diplom;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

public class UserLoginTests {
    protected static List<String> userTokens = new ArrayList();
    protected String userToken;
    protected static UserSteps userSteps;
    String email = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);

    @AfterClass
    public static void tearDown() {
        for (String token : userTokens) {
            if (token != null) {
                userSteps.deleteUserRequest(token).statusCode(SC_ACCEPTED).body("message", is("User successfully removed"));
            }
        }
    }

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_OK)
                .body("success", is(true));

    }

    @Test
    @DisplayName("Успешный логин уникального пользователя с корректными данными")
    @Description("Данный тест покрывает следующие кейсы: 1) пользователя можно создать; 3) чтобы создать пользователя, нужно передать в ручку все обязательные поля; 4) запрос возвращает правильный код ответа (201 Created); 5) успешный запрос возвращает success: true")
    public void loginUserSucessfully() {
        String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
        int spaceIndex = accessToken.indexOf(" "); // Find the index of the space character
        userToken = accessToken.substring(spaceIndex + 1);  // Extract the second part of the string using substring
        userTokens.add(userToken);
    }
}
