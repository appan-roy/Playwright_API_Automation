package com.qa.api.heroku.auth.tests;

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
import utils.CalendarUtils;
import utils.DataGenerator;

import java.io.IOException;

public class UpdateBookingWithTokenTest {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext requestContext;
    String endPoint = "https://restful-booker.herokuapp.com/";
    APIResponse apiResponse;
    int statusCode;
    String statusText;
    private String tokenId;
    String bookingId;

    @BeforeTest
    public void setUp() {

        // creating a playwright context
        playwright = Playwright.create();
        apiRequest = playwright.request();
        requestContext = apiRequest.newContext();

    }

    @Test
    public void createBooking() throws IOException {

        // generate booking data
        String firstName = DataGenerator.generateFirstName();
        String lastName = DataGenerator.generateLastName();
        int totalPrice = DataGenerator.generateNumber(3);
        String checkInDate = CalendarUtils.getDateStamp("yyyy-MM-dd");
        String checkOutDate = CalendarUtils.manipulateDate(CalendarUtils.getDateStamp("yyyy-MM-dd"), "plus", 1, 0, 0, 0);

        // create the request body
        String requestPayload = "{\n" +
                "    \"firstname\": \""+firstName+"\",\n" +
                "    \"lastname\": \""+lastName+"\",\n" +
                "    \"totalprice\": "+totalPrice+",\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \""+checkInDate+"\",\n" +
                "        \"checkout\": \""+checkOutDate+"\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Lunch\"\n" +
                "}";

        // POST call - update the booking
        apiResponse = requestContext.post(endPoint + "booking/", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(requestPayload));

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

        // fetch booking id
        bookingId = jsonNode.get("bookingid").asText();

    }

    @Test
    public void getTokenTest() throws IOException {

        // create the request body
        String requestPayload = """
                {
                    "username" : "admin",
                    "password" : "password123"
                }""";

        // POST call - generate the token
        apiResponse = requestContext.post(endPoint + "auth", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(requestPayload));

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

        // capture the token id
        tokenId = jsonNode.get("token").asText();
        System.out.println("Generated token id : " + tokenId);

        // validate token is not null
        Assert.assertNotNull(tokenId);

    }

    @Test(dependsOnMethods = {"getTokenTest", "createBooking"})
    public void updateBooking() throws IOException {

        // generate booking data
        String firstName = DataGenerator.generateFirstName();
        String lastName = DataGenerator.generateLastName();
        int totalPrice = DataGenerator.generateNumber(3);
        String checkInDate = CalendarUtils.getDateStamp("yyyy-MM-dd");
        String checkOutDate = CalendarUtils.manipulateDate(CalendarUtils.getDateStamp("yyyy-MM-dd"), "plus", 1, 0, 0, 0);

        // create the request body
        String requestPayload = "{\n" +
                "    \"firstname\": \""+firstName+"\",\n" +
                "    \"lastname\": \""+lastName+"\",\n" +
                "    \"totalprice\": "+totalPrice+",\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \""+checkInDate+"\",\n" +
                "        \"checkout\": \""+checkOutDate+"\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"Lunch\"\n" +
                "}";

        // PUT call - update the booking
        apiResponse = requestContext.put(endPoint + "booking/" + bookingId, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Cookie", "token="+tokenId)
                .setData(requestPayload));

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
