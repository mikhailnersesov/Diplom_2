package ru.praktikum.diplom;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

public class UserLoginTests  extends BaseTest  {

    @Override
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
    @Description("Данный тест покрывает следующие кейсы: 1) пользователя можно создать; 2) чтобы создать пользователя, нужно передать в ручку все обязательные поля; 3) запрос возвращает правильный код ответа (201 Created); 4) успешный запрос возвращает success: true")
    public void loginUserSuccessfully() {
        String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
        int spaceIndex = accessToken.indexOf(" "); // Find the index of the space character
        userToken = accessToken.substring(spaceIndex + 1);  // Extract the second part of the string using substring
        userTokens.add(userToken);
    }
}
