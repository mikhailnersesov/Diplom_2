package ru.praktikum.diplom;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.UserSteps;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

import ru.praktikum.diplom.step.UserSteps;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {
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
}
