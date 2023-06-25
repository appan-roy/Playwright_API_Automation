package com.qa.api.gorest.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.DataGenerator;
import utils.User;

import java.io.IOException;

public class PostApiCallUsingPOJOTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;
    String endPoint = "https://gorest.co.in/public/v2/users/";
    APIResponse apiResponse;
    int statusCode;
    String statusText;

    @BeforeTest
    public void setUp() {

        // creating a playwright context
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();

    }

    @Test
    public void createUserTest() throws IOException {

        // generate random data
        String firstName = DataGenerator.generateFirstName();
        String lastName = DataGenerator.generateLastName();
        String fullName = firstName + " " + lastName;
        String emailId = firstName.toLowerCase() + "." + lastName.toLowerCase() + DataGenerator.generateNumber(2) + "@test.com";
        String gender = DataGenerator.generateGender();
        String status = "active";

        // create the request body using pojo class
        User requestUser = new User(fullName, emailId, gender, status);

        // POST call - create the user
        apiResponse = requestContext.post(endPoint, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 89cf87f16f772073ba400974a844aaf551e4086be18a9171e59be21ffa1b662a")
                .setData(requestUser));

        // validate response status code
        statusCode = apiResponse.status();
        System.out.println("Status code: " + statusCode);
        Assert.assertEquals(statusCode, 201);

        // validate response status text
        statusText = apiResponse.statusText();
        System.out.println("Status text: " + statusText);
        Assert.assertEquals(statusText, "Created");

        // print api response in plain text
        System.out.println("----------- api response plain text -------------");
        System.out.println(apiResponse.text());

        // print api response in json format
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        String jsonResponse = jsonNode.toPrettyString();
        System.out.println("-------- api json response ---------");
        System.out.println(jsonResponse);

        // convert json to pojo - deserialization
        User responseUser = objectMapper.readValue(apiResponse.text(), User.class);

        // validate json response against request payload
        Assert.assertEquals(responseUser.getName(), requestUser.getName());
        Assert.assertEquals(responseUser.getEmail(), requestUser.getEmail());
        Assert.assertEquals(responseUser.getGender(), requestUser.getGender());
        Assert.assertEquals(responseUser.getStatus(), requestUser.getStatus());

    }

    @AfterTest
    public void tearDown() {

        // dispose the request context
        requestContext.dispose();

        // dispose the api response
        apiResponse.dispose();

        // close the playwright
        playwright.close();

    }

}
