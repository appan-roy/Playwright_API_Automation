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
import utils.Users;

import java.io.IOException;

public class PutApiCallUsingPOJOWithLombokTest {

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
    public void updateUserTest() throws IOException {

        // generate random data
        String firstName = DataGenerator.generateFirstName();
        String lastName = DataGenerator.generateLastName();
        String fullName = firstName + " " + lastName;
        String emailId = firstName.toLowerCase() + "." + lastName.toLowerCase() + DataGenerator.generateNumber(2) + "@test.com";
        String gender = DataGenerator.generateGender();
        String status = "active";

        // create the request body using pojo lombok
        Users requestUser = Users.builder()
                .name(fullName)
                .email(emailId)
                .gender(gender)
                .status(status).build();

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
        Users responseUser = objectMapper.readValue(apiResponse.text(), Users.class);

        // validate json response against request payload
        Assert.assertEquals(responseUser.getName(), requestUser.getName());
        Assert.assertEquals(responseUser.getEmail(), requestUser.getEmail());
        Assert.assertEquals(responseUser.getGender(), requestUser.getGender());
        Assert.assertEquals(responseUser.getStatus(), requestUser.getStatus());

        // fetch the user id from the POST call response
        int userId = responseUser.getId();
        System.out.println("The created user id is:" + userId);

        // generate random data
        firstName = DataGenerator.generateFirstName();
        lastName = DataGenerator.generateLastName();
        fullName = firstName + " " + lastName;
        emailId = firstName.toLowerCase() + "." + lastName.toLowerCase() + DataGenerator.generateNumber(2) + "@test.com";
        gender = DataGenerator.generateGender();
        status = "inactive";

        // update the user object
        requestUser.setName(fullName);
        requestUser.setEmail(emailId);
        requestUser.setGender(gender);
        requestUser.setStatus(status);

        // PUT call - update the user
        apiResponse = requestContext.put(endPoint + userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 89cf87f16f772073ba400974a844aaf551e4086be18a9171e59be21ffa1b662a")
                .setData(requestUser));

        // validate response status code
        statusCode = apiResponse.status();
        System.out.println("Status code: " + statusCode);
        Assert.assertEquals(statusCode, 200);

        // validate response status text
        statusText = apiResponse.statusText();
        System.out.println("Status text: " + statusText);
        Assert.assertEquals(statusText, "OK");

        // print api response in plain text
        System.out.println("----------- api response plain text -------------");
        System.out.println(apiResponse.text());

        // print api response in json format
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(apiResponse.body());
        jsonResponse = jsonNode.toPrettyString();
        System.out.println("-------- api json response ---------");
        System.out.println(jsonResponse);

        // convert json to pojo - deserialization
        responseUser = objectMapper.readValue(apiResponse.text(), Users.class);

        // validate json response against request payload
        Assert.assertEquals(responseUser.getName(), requestUser.getName());
        Assert.assertEquals(responseUser.getEmail(), requestUser.getEmail());
        Assert.assertEquals(responseUser.getGender(), requestUser.getGender());
        Assert.assertEquals(responseUser.getStatus(), requestUser.getStatus());

        // GET call - fetch the updated user
        apiResponse = requestContext.get(endPoint + userId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 89cf87f16f772073ba400974a844aaf551e4086be18a9171e59be21ffa1b662a"));

        // validate response status code
        statusCode = apiResponse.status();
        System.out.println("Status code: " + statusCode);
        Assert.assertEquals(statusCode, 200);

        // validate response status text
        statusText = apiResponse.statusText();
        System.out.println("Status text: " + statusText);
        Assert.assertEquals(statusText, "OK");

        // print api response in plain text
        System.out.println("----------- api response plain text -------------");
        System.out.println(apiResponse.text());

        // print api response in json format
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree(apiResponse.body());
        jsonResponse = jsonNode.toPrettyString();
        System.out.println("-------- api json response ---------");
        System.out.println(jsonResponse);

        // convert json to pojo - deserialization
        responseUser = objectMapper.readValue(apiResponse.text(), Users.class);

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
