package com.louishoughton;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class GreetingTest
{
    @Test
    @Disabled
    public void testJaxrs() {
        RestAssured.when().get("/hello").then()
                .log().ifValidationFails()
                .contentType("application/json")
                .body("greeting", equalTo("hello + anonymous, isSecure: false, authScheme: null, hasJWT: false"));
    }

}
