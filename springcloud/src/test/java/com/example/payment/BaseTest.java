package com.example.payment;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
public abstract class BaseTest {

        @BeforeEach
        void setup(){
            RestAssured.baseURI="http://localhost:8080";
        }
    }

