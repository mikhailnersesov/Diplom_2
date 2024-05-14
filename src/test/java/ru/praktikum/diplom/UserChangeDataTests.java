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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class UserChangeDataTests {
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
        userSteps
                .createUserRequest(email, password, name)
                .statusCode(SC_OK)
                .body("success", is(true));
        String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
        int spaceIndex = accessToken.indexOf(" "); // Find the index of the space character
        userToken = accessToken.substring(spaceIndex + 1);  // Extract the second part of the string using substring
        userTokens.add(userToken);
    }

    @Test
    @DisplayName("Успешный изменение почты пользователя с авторизацией")
    @Description("Данный тест покрывает следующие кейсы: 1) пройдя авторизацию - можно успешно изменить почту для пользователя")
    public void test_2_changeUserDataEmailWithAuthorizationSuccessfully() {
        String newEmail = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        userSteps.getUserDataRequest(newEmail, name, userTokens.get(0)).statusCode(SC_OK).body("success", is(true)).and().body("user.email", equalToIgnoringCase(newEmail)).and().body("user.name", equalToIgnoringCase(name));
    }

    @Test
    @DisplayName("Успешный изменение имени пользователя с авторизацией")
    @Description("Данный тест покрывает следующие кейсы: 1) пройдя авторизацию - можно успешно изменить пароль для пользователя")
    public void test_1_changeUserDataNameWithAuthorizationSuccessfully() {
        String newName = RandomStringUtils.randomAlphabetic(10);
        userSteps.getUserDataRequest(email, newName, userTokens.get(0)).statusCode(SC_OK).body("success", is(true)).and().body("user.email", equalToIgnoringCase(email)).and().body("user.name", equalToIgnoringCase(newName));
    }

    @Test
    @DisplayName("Успешный изменение всех данных пользователя (почта и имя) с авторизацией")
    @Description("Данный тест покрывает следующие кейсы: 1) пройдя авторизацию - можно успешно изменить сразу и почту и пароль для пользователя")
    public void test_3_changeUserDataBothWithAuthorizationSuccessfully() {
        String newEmail = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        String newName = RandomStringUtils.randomAlphabetic(10);
        userSteps.getUserDataRequest(newEmail, newName, userTokens.get(0)).statusCode(SC_OK).body("success", is(true)).and().body("user.email", equalToIgnoringCase(newEmail)).and().body("user.name", equalToIgnoringCase(newName));
    }

    @Test
    @DisplayName("Ошибка при попытке изменить данные пользователя без авторизации")
    @Description("Данный тест покрывает следующие кейсы: 1) при попытке изменить данные пользователя без авторизации - будет получена ошибка")
    public void test_4_changeUserDataWithoutAuthorizationFailed() {
        String newEmail = "test-data@yandex" + RandomStringUtils.randomAlphabetic(5) + ".ru";
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        userSteps.getUserDataRequest(newEmail, newPassword, "").statusCode(SC_UNAUTHORIZED).body("success", is(false)).and().body("message", is("You should be authorised"));
    }
}
