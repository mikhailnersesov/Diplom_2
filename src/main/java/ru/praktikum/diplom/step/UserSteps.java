package ru.praktikum.diplom.step;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.dto.UserCreateRequest;

public class UserSteps {
    private final UserClient userClient;

    public UserSteps(UserClient userClient) {
        this.userClient = userClient;
    }
    @Step("Создание уникального пользователя")
    public ValidatableResponse createUserRequest(String email, String password, String name) {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        return userClient.sendPostRequestUserRegister(userCreateRequest).then();
    }
}
