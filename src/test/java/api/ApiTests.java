package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void testGetCommentsByPostId_Positive() {
        Response response = given()
                .queryParam("postId", 1)
                .when()
                .get("/comments");

        Assertions.assertEquals(200, response.getStatusCode(), "Code is not 200");
        Assertions.assertTrue(response.jsonPath().getList("$").size() > 0, "There is nothing in the comment list");
        Assertions.assertEquals(1, response.jsonPath().getInt("[0].postId"), "First element PostId is not 1");
    }

    @Test
    public void testGetCommentsWithInvalidPostId() {
        Response response = given()
                .queryParam("postId", 9999)
                .when()
                .get("/comments");

        Assertions.assertEquals(200, response.getStatusCode(), "Code is not 200");
        Assertions.assertEquals(0, response.jsonPath().getList("$").size(), "Comments array is not empty");
    }

    @Test
    public void testGetCommentsWithoutPostId() {
        given()
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(10));
    }

    @Test
    public void testGetCommentsWithInvalidPostIdType() {
        given()
                .queryParam("postId", "abc")
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @Test
    public void testInvalidCommentsEndpoint() {
        given()
                .when()
                .get("/wrong-comments-endpoint")
                .then()
                .statusCode(404);
    }
}
