package ru.praktikum.diplom;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class UserChangeDataTests extends BaseTest {

    @Override
    @After
    public void tearDownMethod() {
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
