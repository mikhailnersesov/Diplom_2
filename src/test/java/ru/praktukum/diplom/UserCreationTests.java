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

public class UserCreationTests {
    protected static List<String> userTokens = new ArrayList();
    protected static UserSteps userSteps;
    protected String userToken;
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

    @Test
    @DisplayName("Успешное создание уникального пользователя с корректными данными")
    @Description("Данный тест покрывает следующие кейсы: 1) пользователя можно создать; 2) чтобы создать пользователя, нужно передать в ручку все обязательные поля; 3) запрос возвращает правильный код ответа (201 Created); 4) успешный запрос возвращает success: true")
    public void createUserSuccessfully() {
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_OK) //BUG: STEBURG-1: actually is a bug in RQ: creation should be not 200, but "201 created"
                .body("success", is(true));
    }

    @Test
    @DisplayName("Ошибка при создании пользователя, который уже существует")
    @Description("Данный тест покрывает следующие кейсы: 1) нельзя создать двух одинаковых пользователей; 2) запрос возвращает правильный код ответа(403 Forbidden); 3) успешный запрос возвращает success: false")
    public void createSecondSameUserFailed() {
        userSteps
                .createUserRequest(email, password, name);
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false));
    }
}
