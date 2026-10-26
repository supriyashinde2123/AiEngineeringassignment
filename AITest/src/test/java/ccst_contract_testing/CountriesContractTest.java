package ccst_contract_testing;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class CountriesContractTest {
    private static final String VALID_TOKEN = "rc_live_0699a19f6e6a4cb098cc3ac608ab1c9c";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.restcountries.com";
        RestAssured.basePath = "/countries/v5";
        RestAssured.port = -1;
    }

    @Test
    public void statusIs200() {
        given()
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void responseTimeUnder2000ms() {
        given()
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .when()
                .get()
                .then()
                .time(lessThan(2000L));
    }

    @Test
    public void bodyMatchesSchema() {
        given()
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .when()
                .get()
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("country-schema.json"));
    }

    @Test
    public void noAuthorizationHeaderReturns401() {
        given()
                .when()
                .get()
                .then()
                .statusCode(401);
    }

    @Test
    public void bearerWrongKeyReturns401() {
        given()
                .header("Authorization", "Bearer wrong-key")
                .when()
                .get()
                .then()
                .statusCode(401);
    }
}
