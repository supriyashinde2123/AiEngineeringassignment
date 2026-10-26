package com.example.payment;

import com.example.payment.BaseTest;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.RestAssured.*;

@SuppressWarnings("rawtypes")
public class ContractVerifierTest extends BaseTest {

	@Test
	public void validate_get_payment() throws Exception {
		// given:
			RequestSpecification request = given();


		// when:
			Response response = given().spec(request)

					.get("/payment/1");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);



		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['id']").isEqualTo(1);
			assertThatJson(parsedJson).field("['status']").isEqualTo("PAID");
	}

}
