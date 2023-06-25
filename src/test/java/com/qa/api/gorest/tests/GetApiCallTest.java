package com.qa.api.gorest.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetApiCallTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;
    String endPoint = "https://gorest.co.in/public/v2/users";
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
    public void getAllUsersApiTest() throws IOException {

        // GET call
        apiResponse = requestContext.get(endPoint);

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
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        String jsonResponse = jsonNode.toPrettyString();
        System.out.println("-------- api json response ---------");
        System.out.println(jsonResponse);

        // print the endpoint of the response
        System.out.println("Api endpoint: " + apiResponse.url());
        Assert.assertEquals(apiResponse.url(), "https://gorest.co.in/public/v2/users");

        // fetch response headers using map
        System.out.println("-------- api headers using map ---------");
        Map<String, String> headersMap = apiResponse.headers();
        System.out.println(headersMap);
        headersMap.forEach((k, v) -> System.out.println(k + " : " + v));

        // fetch response headers using list
        System.out.println("-------- api headers using list ---------");
        List<HttpHeader> headersList = apiResponse.headersArray();
        for (HttpHeader header : headersList) {
            System.out.println(header.name + " : " + header.value);
        }

        // validate response headers
        Assert.assertEquals(headersMap.size(), 29);
        Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");
        Assert.assertEquals(headersList.size(), 29);

    }

    @Test
    public void getSpecificUserApiTest() throws IOException {

        // GET call with query params
        apiResponse = requestContext.get(endPoint, RequestOptions.create().setQueryParam("gender", "male").setQueryParam("status", "active"));

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
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(apiResponse.body());
        String jsonResponse = jsonNode.toPrettyString();
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
