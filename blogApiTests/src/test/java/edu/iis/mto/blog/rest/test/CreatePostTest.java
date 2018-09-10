package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreatePostTest extends FunctionalTests {

    private static final String HEADER_1 = "Content-Type";
    private static final String HEADER_2 = "application/json;charset=UTF-8";
    private static final String CONFIRMED_USER_POST_API = "/blog/user/1/post";
    private static final String NEW_USER_POST_API = "/blog/user/2/post";

    @Test
    public void confirmedUserShouldBeAbleToPost() {

        JSONObject post = new JSONObject().put("entry", "Confirmed");

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .body(post.toString())
                .expect().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .when().post(CONFIRMED_USER_POST_API);
    }

    @Test
    public void newUserShouldNotBeAbleToPost() {

        JSONObject post = new JSONObject().put("entry", "New");

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .body(post.toString())
                .expect().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(NEW_USER_POST_API);
    }
}
