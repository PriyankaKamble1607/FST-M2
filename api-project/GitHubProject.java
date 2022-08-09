import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class ProjectTest1 {
    RequestSpecification requestSpec;
    String token = "token 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";
    int SSHId;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", token)
                .setBaseUri("https://api.github.com")
                .build();
    }

    @Test(priority = 1)
    public void PostSShKey() {
        String reqBody = "{\"title\": \"TestAPIKey\",\"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCrAo2utUYdNJ1XosjnEBaH1Bf92lkVuzg95gcBqMrpjcHxNyGr3AzuHh5PQUBAJYKKlU/hNkKE3XRYHp6npzzgC/bf0v851DHYlEd3I26nWAgIAJc3tLhFSwxVf+oOS91+UJoqYjjC8TGObJ1HUhDRQ4y19ZoXn7B5/un0rNnfj0heFSu5dc3reK6zNRXYEuut//u4fNJ14IHu+oPWqxwKDxQxe2g+XpJZu/JWzN+k7uhujWN+SPNjoNdYe2P0YsfXw7fPCQt6jG4KgyKYFuB4e1fZmK01gDYkW5ADV4ddUedKXmKpp7Lg1cwy677z2KhhQVXGCvXzYe3LVp8aVNi/\"}";

        Response response = given().spec(requestSpec) // Use requestSpec
                .body(reqBody) // Send request body
                .when().post("/user/keys");// Send POST request
        //System.out.println(response.getBody().asPrettyString());
        SSHId = response.then().extract().path("id");
        System.out.println(SSHId);
        response.then().statusCode(201); //post always has response 201
    }

    @Test(priority = 2)
    public void GetRequest() {
//always 200 status code
        Response response = given().spec(requestSpec)
                .pathParam("keyId", SSHId)
                .when().get("/user/keys/{keyId}"); // Send GET request

        System.out.println(response.getBody().asPrettyString());
        Reporter.log(response.getBody().asPrettyString());
        response.then().statusCode(200);
    }

    @Test(priority = 3)
    public void Delete() {
        Response response = given().spec(requestSpec)
                .pathParam("keyId", SSHId) // Add path parameter
                .when().delete("/user/keys/{keyId}"); // Send GET request


        System.out.println(response.getBody().asPrettyString());
        Reporter.log(response.getBody().asPrettyString());
        // Assertions
        response.then().statusCode(204);
    }

}
