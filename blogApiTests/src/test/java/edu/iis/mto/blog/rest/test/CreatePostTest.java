package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreatePostTest extends FunctionalTests {

    private static final String HEADER_1 = "Content-Type";
    private static final String HEADER_2 = "application/json;charset=UTF-8";

    @Test
    public void onlyConfirmedUserShouldBeAbleToPost() {

        JSONObject postOne = new JSONObject().put("entry", "First");
        JSONObject postTwo = new JSONObject().put("entry", "Second");

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .body(postOne.toString())
                .expect().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .when().post("Confirmed user");

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .body(postTwo.toString())
                .expect().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post("New user");
    }
}
