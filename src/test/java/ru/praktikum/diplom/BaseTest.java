package ru.praktikum.diplom;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import ru.praktikum.diplom.client.UserClient;
import ru.praktikum.diplom.step.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

public class BaseTest {
    protected static List<String> userTokens = new ArrayList();
    protected static UserSteps userSteps;
    protected String userToken;
    private static Faker faker = new Faker();

    public String email= faker.internet().emailAddress();;
    public String password= faker.internet().password(6, 12);
    public String name= faker.name().lastName();


    @Before
    public void setUpMethod() {

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

    @After
    public void tearDownMethod() {
        try {
            String accessToken = userSteps.loginUserRequest(email, password).statusCode(SC_OK).extract().path("accessToken");
            int spaceIndex = accessToken.indexOf(" "); // Find the index of the space character
            userToken = accessToken.substring(spaceIndex + 1);  // Extract the second part of the string using substring
            userTokens.add(userToken);
        } catch (AssertionError assertionError) {
            System.out.println("no users was created - nothing to save");
        }
    }

    @AfterClass
    public static void tearDownClass() {
        for (String token : userTokens) {
            if (token != null) {
                userSteps.deleteUserRequest(token).statusCode(SC_ACCEPTED).body("message", is("User successfully removed"));
            }
        }
        userTokens.clear();
    }
}
