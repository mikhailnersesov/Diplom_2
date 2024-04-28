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

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

public class UserCreationTests {
    protected static UserSteps userSteps;
    String email = "test-data@yandex12343.ru";
    String password = RandomStringUtils.randomAlphabetic(10);
    String name = RandomStringUtils.randomAlphabetic(10);
    @AfterClass
    public static void tearDown() {
//TODO add deletion of the user
    }
    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }
    @After
    public void getUserIdIfWasSucessfullyCreated() {}

    @Test
    @DisplayName("Успешное создание пользователя с корректными данными")
    @Description("Данный тест покрывает следующие кейсы: 1) курьера можно создать; 3) чтобы создать курьера, нужно передать в ручку все обязательные поля; 4) запрос возвращает правильный код ответа (201 Created); 5) успешный запрос возвращает ok: true")
    public void createUserSucessfully() {
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_OK)
                .body("success", is(true)).log().all();
    }
}
