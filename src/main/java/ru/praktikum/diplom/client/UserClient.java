package ru.praktikum.diplom.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom.dto.UserCreateRequest;
import ru.praktikum.diplom.dto.UserDataChangeRequest;
import ru.praktikum.diplom.dto.UserLoginRequest;

import static ru.praktikum.diplom.config.UserConfig.*;

public class UserClient extends RestClient {
    @Step("Send POST request to /auth/register")
    public Response sendPostRequestUserRegister(UserCreateRequest userCreateRequest) {
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post(REGISTER_ENDPOINT);
    }

    @Step("Send POST request to /auth/login")
    public Response sendPostRequestUserLogin(UserLoginRequest userLoginRequest) {
        return getDefaultRequestSpecification()
                .body(userLoginRequest)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Send DELETE request to /auth/user")
    public Response sendDeleteRequestUserDeletion(String accessToken) {
        return getDefaultRequestSpecification().auth().oauth2(accessToken)
                .when()
                .delete(STATE_CHANGE_ENDPOINT);
    }

    @Step("Send PATCH request to /auth/user")
    public Response sendPatchRequestGetUserData(UserDataChangeRequest userDataChangeRequest, String accessToken) {
        return getDefaultRequestSpecification().auth().oauth2(accessToken)
                .body(userDataChangeRequest)
                .when()
                .patch(STATE_CHANGE_ENDPOINT);
    }
}
