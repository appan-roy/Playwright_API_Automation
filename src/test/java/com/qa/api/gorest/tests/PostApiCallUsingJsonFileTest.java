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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PostApiCallUsingJsonFileTest {

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

        // read json file - every time change the email in the user.json file (not a good approach)
        File file = new File("./src/test/data/user.json");
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        // POST call - create the user
        apiResponse = requestContext.post(endPoint, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 89cf87f16f772073ba400974a844aaf551e4086be18a9171e59be21ffa1b662a")
                .setData(fileBytes));

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

        // capture the id
        String userId = jsonNode.get("id").asText();
        System.out.println("Generated user id : " + userId);

        // GET call - validate the user is created or not
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
