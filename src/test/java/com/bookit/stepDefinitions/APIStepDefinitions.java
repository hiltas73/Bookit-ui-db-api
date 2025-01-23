package com.bookit.stepDefinitions;

import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtil;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class APIStepDefinitions {

    String token;
    Response response;
    String globalEmail;

    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {
        token = BookitUtils.generateTokenByRole(role);
        // print the token returned from the previous line
        System.out.println("Generated token: " + token);

        // just checking if the environment changes from config.prop file working
        // String baseUrl = Environment.BASE_URL;
        // System.out.println("baseUrl = " + baseUrl);

        Map<String, String> credentialsMap = BookitUtils.returnCredentials(role);
        globalEmail = credentialsMap.get("email");


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

    @Then("the information about current user from api and database should match")
    public void theInformationAboutCurrentUserFromApiAndDatabaseShouldMatch() {

        // get firstName + lastName + role from api as actual data
        String actualFirstName = response.path("firstName");
        String actualLastName = response.path("lastName");
        String actualRole = response.path("role");

        // get data from database
        String query = "select firstname,lastname,role from users\n" +
                "where email='" + globalEmail + "'";

        // run query
        DBUtil.runQuery(query);

        // get the result inside a map
        Map<String, String> dbMap = DBUtil.getRowMap(1);

        // display all data from the db
        DBUtil.displayAllData();

        // compare the actual data with the expected data from the database
        assertEquals(actualFirstName, dbMap.get("firstname"));
        assertEquals(actualLastName, dbMap.get("lastname"));
        assertEquals(actualRole, dbMap.get("role"));

        // destroy the connection
        DBUtil.destroy();


    }
}
