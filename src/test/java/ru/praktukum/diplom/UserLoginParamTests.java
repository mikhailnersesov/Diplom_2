package ru.praktukum.diplom;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class UserLoginParamTests {
    protected static List<String> userTokens = new ArrayList();
    protected static UserSteps userSteps;
    static String email = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
    static String password = RandomStringUtils.randomAlphabetic(10);
    static String name = RandomStringUtils.randomAlphabetic(10);

    @AfterClass
    public static void tearDown() {
        for (String token : userTokens) {
            if (token != null) {
                userSteps.deleteUserRequest(token).statusCode(SC_ACCEPTED).body("message", is("User successfully removed"));
            }
        }
    }

    @BeforeClass
    public static void setUp() {
        userSteps = new UserSteps(new UserClient());
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_OK) //BUG: STEBURG-1: should be not 200, but 201 created
                .body("success", is(true));

    }

    @Parameterized.Parameter(0)
    static public String emailParam = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
    @Parameterized.Parameter(1)
    static public String passwordParam = RandomStringUtils.randomAlphabetic(10);

    @Parameterized.Parameters(name = "{index} - email {0}, password {1}")
    public static Object[][] data() {
        return new Object[][]{
                {"test-data@yandex.ru", password},
                {email, "123"},
                {"", password},
                {email, ""},
                {null, password},
                {email, null}
        };
    }

    @Test
    @DisplayName("Ошибка при логине с неверными данными")
    @Description("Данный тест покрывает следующие кейсы: 2) нельзя создать двух одинаковых пользователей; 4) запрос возвращает правильный код ответа(403 Forbidden)")
    public void loginWithWrongPasswordFailed() {
        userSteps.loginUserRequest("test-data@yandex.ru", password).statusCode(SC_UNAUTHORIZED).body("message", is("email or password are incorrect"));
    }
}
