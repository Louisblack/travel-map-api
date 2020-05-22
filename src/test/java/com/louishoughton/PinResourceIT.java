package com.louishoughton;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@QuarkusTest
public class PinResourceIT {

    private String token;

    @Inject
    DynamoDbClient client;
    private final CreateTableRequest createTableRequest;

    public PinResourceIT() throws IOException {
        createTableRequest = new DynamoDbJsonToRequest().fromJson("/infra/table.json");
    }

    @BeforeEach
    public void generateToken() throws Exception {
        HashMap<String, Long> timeClaims = new HashMap<>();
        token = TokenUtils.generateTokenString("/claims.json", timeClaims);
        System.out.println(token);
        client.createTable(createTableRequest);
    }

    @AfterEach
    void tearDown() {
        client.deleteTable(DeleteTableRequest.builder().tableName(createTableRequest.tableName()).build());
    }

    @Test
    public void should_save_and_get_pins() {
        Pin pin = new Pin();
        pin.setLat(123.45);
        pin.setLon(6543.76);
        pin.setDescription("test");

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .auth().oauth2(token)
                    .body(pin)
                .when()
                    .post("/pins")
                .then()
                    .statusCode(200);

        RestAssured.given()
                    .auth().oauth2(token)
                .when()
                    .get("/pins")
                .then()
                    .assertThat().body("$.size", not(is(0)));
    }
}
