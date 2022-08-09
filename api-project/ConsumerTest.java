package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.Consumer;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.ibm.icu.impl.UResource;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //headers
    Map<String, String> reqHeaders = new HashMap<>();
    String resourcePath = "/api/users";

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        //set headers
        reqHeaders.put("Content-Type", "application/json");
        //create body
        DslPart bodySentCreateUser = new PactDslJsonBody()
                .numberType("id", 1)
                .stringType("firstName", "Justin")
                .stringType("lastName", "Case")
                .stringType("email", "justincase@mail.com");

        //create the contract
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .path(resourcePath)
                .method("POST")
                .headers(reqHeaders)
                .body(bodySentCreateUser)
                .willRespondWith()
                .status(201)
                .body(bodySentCreateUser)
                .toPact();

    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")//mock server
    public void consumerTest() {
        //set baseURI
        String baseURI = "http://localhost:8282";
        //set request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 1);
        reqBody.put("firstName", "Justin");
        reqBody.put("lastName", "Case");
        reqBody.put("email", "justincase@mail.com");

        //generate Response
        Response response = given().headers(reqHeaders).body(reqBody)
                .when().post(baseURI + resourcePath);
        System.out.println(response.getBody().asPrettyString());
        // Assertion
        response.then().statusCode(201);
    }
}
