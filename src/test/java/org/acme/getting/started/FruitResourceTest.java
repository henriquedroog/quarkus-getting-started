package org.acme.getting.started;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
//@QuarkusTestResource(H2DatabaseTestResource.class)
public class FruitResourceTest {

    private static final String fruitBaseUrl = "/fruits";

    @Test
    public void testGetFruits() {
        given()
          .when().get(fruitBaseUrl)
          .then()
             .statusCode(200);
    }
}