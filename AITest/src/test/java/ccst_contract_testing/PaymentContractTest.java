package ccst_contract_testing;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
public class PaymentContractTest {

    @BeforeClass
    public void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }


    @Test
    public void statusIs200(){
        given()
                .when()
                .get("/payment/2")
                .then()
                .statusCode(200);
    }



    @Test
    public void responseTimeUnder1000ms(){
        given()
                .when()
                .get("/payment/2")
                .then()
                .time(lessThan(1000L));
    }



    @Test
    public void hasIdAndStatus(){
        given()
                .when()
                .get("/payment/2")
                .then()
                .body("id", notNullValue())
                .body("status", notNullValue());
    }


    @Test
    public void matchesContractSchema(){
        given()
                .when()
                .get("/payment/2")
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("payment-schema.json"));
    }

    @Test
    public void unknownPaymentReturns404(){
        given()
                .when()
                .get("/payment/9999")
                .then()
                .statusCode(404);
    }
}