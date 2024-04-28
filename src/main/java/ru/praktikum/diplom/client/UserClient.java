package ru.praktikum.diplom.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.diplom.dto.UserCreateRequest;

public class UserClient extends RestClient {
    @Step("Send POST request to /auth/register")
    public Response sendPostRequestUserRegister(UserCreateRequest userCreateRequest) {
        return getdefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post("/auth/register");
    }
}
