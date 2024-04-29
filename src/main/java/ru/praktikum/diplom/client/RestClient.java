package ru.praktikum.diplom.client;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.praktikum.diplom.dto.UserDeleteRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.diplom.config.RestConfig.BASE_URI;

public abstract class RestClient {
    protected RequestSpecification getdefaultRequestSpecification() {
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON).log().all();
    }
}
