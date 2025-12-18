package com.mapping.task.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ControllerIntegrationTest {

    private static final String LOCALHOST = "http://localhost:";
    private static final String MAP_PATH = "/map";

    @LocalServerPort
    public int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getMappingWithOneNumberOnListShouldPass() {
        //given
        String expectedResult = "{\"4\":[\"Mazda\",\"BMW\",\"Toyota\"]}";
        String url = LOCALHOST + port + MAP_PATH + "?category=Car&numberLists=4";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getMappingWithListOfNumbersShouldPass() {
        //given
        String expectedResult = "{\"1\":[\"Poland\"],\"2\":[\"Poland\",\"Germany\"],\"4\":[\"Poland\",\"Germany\",\"Italy\"],\"15\":[\"Poland\",\"France\",\"Spain\",\"Czechia\"],\"20\":[\"Poland\",\"Germany\",\"Italy\",\"Spain\",\"Denmark\",\"Canada\"]}";
        String url = LOCALHOST + port + MAP_PATH + "?category=Country&numberLists=1,2,4,15,20";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getMappingWithDuplicatedNumberShouldPass() {
        //given
        String expectedResult = "{\"1\":[\"Poland\"],\"2\":[\"Poland\",\"Germany\"]}";
        String url = LOCALHOST + port + MAP_PATH + "?category=Country&numberLists=1,2,2";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void getMappingWithBlankCategoryShouldFail() {
        //given
        String url = LOCALHOST + port + MAP_PATH + "?category=&numberLists=1,2,4,15,20";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Provided category is blank!", response.getBody());
    }

    @Test
    void getMappingWithIncorrectCategoryShouldFail() {
        //given
        String url = LOCALHOST + port + MAP_PATH + "?category=IncorrectCategory&numberLists=1,2,4,15,20";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Header name 'IncorrectCategory' not found. Available columns are: [Divisor, Animals, Furniture, Car, Fruit, Country, Color, Job, Sport, City, Technology]", response.getBody());
    }

    @Test
    void getMappingWithEmptyNumberListShouldFail() {
        //given
        String url = LOCALHOST + port + MAP_PATH + "?category=Car&numberLists=";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Provided numberLists is blank!", response.getBody());
    }

    @Test
    void getMappingWithStringInNumberListShouldFail() {
        //given
        String url = LOCALHOST + port + MAP_PATH + "?category=Car&numberLists=1,2,String,4";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Provided value {String} can't be parsed to number", response.getBody());
    }

    @Test
    void getMappingWithNumberGreaterThan20ShouldFail() {
        //given
        String url = LOCALHOST + port + MAP_PATH + "?category=Car&numberLists=22";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Provided {22} number can't be greater than 20. Accepted range is 1-20", response.getBody());
    }

    @Test
    void getMappingWithNumberLowerThan1ShouldFail() {
        //given
        String url = LOCALHOST + port + MAP_PATH + "?category=Car&numberLists=-5";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Provided {-5} number can't be lower than 1. Accepted range is 1-20", response.getBody());
    }
}
