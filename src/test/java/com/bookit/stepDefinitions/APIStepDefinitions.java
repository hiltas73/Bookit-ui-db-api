package com.bookit.stepDefinitions;

import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class APIStepDefinitions {

    String token;
    Response response;

    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {
        token = BookitUtils.generateTokenByRole(role);
        // print the token returned from the previous line
        System.out.println("Generated token: " + token);

        // just checking if the environment changes from config.prop file working
        // String baseUrl = Environment.BASE_URL;
        // System.out.println("baseUrl = " + baseUrl);


    }
    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {
        response = given().accept(ContentType.JSON)
                .header("Authorization",token)
                .when().get(Environment.BASE_URL + endpoint);
    }
    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        int actualStatusCode = response.statusCode();
        // System.out.println("actualStatusCode = " + actualStatusCode);
        // System.out.println("expectedStatusCode = " + expectedStatusCode);
        assertEquals("status code test", expectedStatusCode, actualStatusCode);
    }
    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {
        String actualContentType = response.getContentType();
        // System.out.println("actualContentType = " + actualContentType);
        // System.out.println("expectedContentType = " + expectedContentType);
        assertEquals("content type test", expectedContentType, actualContentType);
    }
    @Then("role is {string}")
    public void role_is(String expectedRole) {
        String actualRole = response.path("role");
        // System.out.println("actualRole = " + actualRole);
        // System.out.println("expectedRole = " + expectedRole);
        assertEquals(expectedRole, actualRole);
        // response.prettyPrint();
    }

}
