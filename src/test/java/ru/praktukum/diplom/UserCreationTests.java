package ru.praktukum.diplom;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class UserCreationTests {
    protected static UserSteps userSteps;
    String email = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
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
    @DisplayName("Успешное создание уникального пользователя с корректными данными")
    @Description("Данный тест покрывает следующие кейсы: 1) пользователя можно создать; 3) чтобы создать пользователя, нужно передать в ручку все обязательные поля; 4) запрос возвращает правильный код ответа (201 Created); 5) успешный запрос возвращает success: true")
    public void createUserSucessfully() {
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_OK) //BUG: STEBURG-1: should be not 200, but 201 created
                .body("success", is(true)).log().all();
    }
    @Test
    @DisplayName("Ошибка при создании пользователя, который уже существует")
    @Description("Данный тест покрывает следующие кейсы: 2) нельзя создать двух одинаковых пользователей; 4) запрос возвращает правильный код ответа(403 Forbidden)")
    public void createSecondSameUserFailed() {
        userSteps
                .createUserRequest(email, password, name);
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false));
    }
    @Parameterized.Parameter(0)
    static public String emailParam = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
    @Parameterized.Parameter(1)
    static public String passwordParam = RandomStringUtils.randomAlphabetic(10);
    @Parameterized.Parameter(2)
    static public String nameParam = RandomStringUtils.randomAlphabetic(10);

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {"",passwordParam, nameParam},
                {emailParam,"", nameParam},
                {emailParam,passwordParam, ""}, //TODO try with null?
        };
    }
    @Test
    @DisplayName("Ошибка при создании пользователя, без одного из обязательных полей")
    @Description("Данный тест покрывает следующие кейсы: 2) нельзя создать двух одинаковых пользователей; 4) запрос возвращает правильный код ответа(403 Forbidden)")
    public void createUserWithoutMandatoryParameterFailed() {

        userSteps
                .createUserRequest(emailParam, passwordParam, nameParam)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false));
    }
}
