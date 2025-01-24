package com.bookit.stepDefinitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtil;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

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

        response.prettyPrint();
        //GET DATA FROM API
        JsonPath jsonPath = response.jsonPath();
        /*
        {
            "id": 17381,
            "firstName": "Raymond",
            "lastName": "Reddington",
            "role": "student-team-member"
        }
         */
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //GET DATA FROM DATABASE
        //first we need to create database connection which will handle by custom hooks
        String query = "select firstname,lastname,role from users\n" +
                "where email ='" + globalEmail + "'";
        //run your query
        DBUtil.runQuery(query);

        //get the result to map
        Map<String, String> dbMap = DBUtil.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String expectedFirstName = dbMap.get("firstname");
        String expectedLastName = dbMap.get("lastname");
        String expectedRole = dbMap.get("role");

        //COMPARE API vs DB

        assertEquals(expectedFirstName,actualFirstName);
        assertEquals(expectedLastName,actualLastName);
        assertEquals(expectedRole,actualRole);


    }

    @Then("UI,API and Database user information must be match")
    public void uiAPIAndDatabaseUserInformationMustBeMatch() {

        // prettyPrint the response
        response.prettyPrint();

        // GET data from API using jsonPath()
        String actualFirstNameAPI = response.jsonPath().getString("firstName");
        String actualLastNameAPI = response.jsonPath().getString("lastName");
        String actualRoleAPI = response.jsonPath().getString("role");

        // GET data from database
        String query = "select firstname,lastname,role from users\n" +
                "where email='" + globalEmail + "'";

        // run query
        DBUtil.runQuery(query);

        // get the result inside a map
        Map<String, String> dbMap = DBUtil.getRowMap(1);

        // display all data from the db
        DBUtil.displayAllData();

        String expectedFirstNameDB = dbMap.get("firstname");
        String expectedLastNameDB = dbMap.get("lastname");
        String expectedRoleDB = dbMap.get("role");

        // compare the actual data from API with the expected data from the database
        assertEquals(expectedFirstNameDB, actualFirstNameAPI);
        assertEquals(expectedLastNameDB, actualLastNameAPI);
        assertEquals(expectedRoleDB, actualRoleAPI);


        // COMPARE UI vs DB
        // get data from UI using selfPage object
        SelfPage selfPage = new SelfPage();
        String actualFullNameUI = selfPage.name.getText();
        String actualRoleUI = selfPage.role.getText();

        // UI vs DB
        // get firstName and lastName from API and assign them to a new String variable as expectedFullName
        String expectedFullNameDB = expectedFirstNameDB + " " + expectedLastNameDB;
        // compare expectedFullName with actualFullNameUI
        assertEquals(expectedFullNameDB, actualFullNameUI);
        // compare expectedRole with actualRoleUI
        assertEquals(expectedRoleDB, actualRoleUI);

        // UI vs API
        // UI --> actual    API --> expected
        // get firstName and lastName from API and assign them to a new String variable as expectedFullName
        String expectedFullNameAPI = actualFirstNameAPI + " " + actualLastNameAPI;
        // compare expectedFullNameAPI with actualFullNameUI
        assertEquals(expectedFullNameAPI, actualFullNameUI);

        // get actualRoleAPI from above and assign it to String expectedRoleAPI
        String expectedRoleAPI = actualRoleAPI;
        // compare expectedRoleAPI with actualRoleUI
        assertEquals(expectedRoleAPI, actualRoleUI);


        // destroy the connection
        DBUtil.destroy();

    }
}
