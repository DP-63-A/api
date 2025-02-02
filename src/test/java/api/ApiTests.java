package api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void testGetCommentsByPostId_Positive() {
        given()
                .queryParam("postId", 1)
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].postId", equalTo(1))
                .body("[0].email", not(emptyOrNullString()))
                .body("[0].name", not(emptyOrNullString()));
    }

    @Test
    public void testGetCommentsWithInvalidPostId() {
        given()
                .queryParam("postId", 9999)
                .when()
                .get("/comments")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
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
