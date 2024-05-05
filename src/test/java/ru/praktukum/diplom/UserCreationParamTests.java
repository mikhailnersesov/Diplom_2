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

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class UserCreationParamTests {
    protected static List<String> userTokens = new ArrayList();
    protected String userToken;
    protected static UserSteps userSteps;

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
            String accessToken = userSteps.loginUserRequest(emailParam, passwordParam).statusCode(SC_OK).extract().path("accessToken");
            int spaceIndex = accessToken.indexOf(" "); // Find the index of the space character
            userToken = accessToken.substring(spaceIndex + 1);  // Extract the second part of the string using substring
        } catch (AssertionError assertionError) {
            System.out.println("no users was created - nothing to save");
        }
        userTokens.add(userToken);
    }

    @Parameterized.Parameter(0)
    static public String emailParam = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
    @Parameterized.Parameter(1)
    static public String passwordParam = RandomStringUtils.randomAlphabetic(10);
    @Parameterized.Parameter(2)
    static public String nameParam = RandomStringUtils.randomAlphabetic(10);

    @Parameterized.Parameters(name = "{index} - email {0}, password {1}, name {2}")
    public static Object[][] data() {
        return new Object[][]{
                {"", passwordParam, nameParam},
                {null, passwordParam, nameParam},
                {emailParam, "", nameParam},
                {emailParam, null, nameParam},
                {emailParam, passwordParam, ""},
                {emailParam, passwordParam, null}
        };
    }

    @Test
    @DisplayName("Ошибка при создании пользователя, без одного из обязательных полей")
    @Description("Данный тест покрывает следующие кейсы: 1) нельзя создать пользователя без одного из обязательных полей; 2) запрос возвращает правильный код ответа(403 Forbidden); 3) успешный запрос возвращает success: false")
    public void createUserWithoutMandatoryParameterFailed() {

        userSteps
                .createUserRequest(emailParam, passwordParam, nameParam)
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false));
    }
}
