package ru.praktikum.diplom.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom.dto.UserCreateRequest;
import ru.praktikum.diplom.dto.UserDataChangeRequest;
import ru.praktikum.diplom.dto.UserLoginRequest;

public class UserClient extends RestClient {
    @Step("Send POST request to /auth/register")
    public Response sendPostRequestUserRegister(UserCreateRequest userCreateRequest) {
        return getdefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post("/auth/register");
    }

    @Step("Send POST request to /auth/login")
    public Response sendPostRequestUserLogin(UserLoginRequest userLoginRequest) {
        return getdefaultRequestSpecification()
                .body(userLoginRequest)
                .when()
                .post("/auth/login");
    }

    @Step("Send DELETE request to /auth/user")
    public Response sendDeleteRequestUserDeletion(String accessToken) {
        return getdefaultRequestSpecification().auth().oauth2(accessToken)
                .when()
                .delete("/auth/user");
    }

    @Step("Send PATCH request to /auth/user")
    public Response sendPatchRequestGetUserData(UserDataChangeRequest userDataChangeRequest, String accessToken) {
        return getdefaultRequestSpecification().auth().oauth2(accessToken)
                .body(userDataChangeRequest)
                .when()
                .patch("/auth/user");
    }
}
