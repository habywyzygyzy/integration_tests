package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static org.hamcrest.Matchers.hasItem;

public class LikePostTest extends FunctionalTests {

    private static final String CONFIRMED_USER_LIKE_OWN_POST_API = "/blog/user/1/like/1";
    private static final String CONFIRMED_USER_LIKE_SOMEONES_POST_API = "/blog/user/3/like/1";
    private static final String NEW_USER_LIKE_SOMEONES_POST_API = "/blog/user/2/like/1";
    private static final String CONFIRMED_USER_POST_API = "/blog/user/1/post";

    private static final String HEADER_1 = "Content-Type";
    private static final String HEADER_2 = "application/json;charset=UTF-8";

    @Test
    public void confirmedUserShouldNotBeAbleToLikeOwnPost() {

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(CONFIRMED_USER_LIKE_OWN_POST_API);
    }

    @Test
    public void confirmedUserShouldBeAbleToLikeAnotherUserPost() {

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_OK)
                .when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);
    }

    @Test
    public void newUserShouldNotBeAbleToLikeAnotherUserPost() {

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when().post(NEW_USER_LIKE_SOMEONES_POST_API);
    }

    @Test
    public void likingSamePostAgainShouldNotChangeItsStatus() {

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_OK)
                .when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);

        int counterAfterFirstLikeAttempt = RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_OK)
                .when().get(CONFIRMED_USER_POST_API)
                .then().extract().jsonPath().getInt("likesCount[0]");

        RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_OK)
                .when().post(CONFIRMED_USER_LIKE_SOMEONES_POST_API);

        int counterAfterSecondLikeAttempt = RestAssured.given().accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .expect().log().all()
                .statusCode(HttpStatus.SC_OK)
                .when().get(CONFIRMED_USER_POST_API)
                .then().extract().jsonPath().getInt("likesCount[0]");

        Assert.assertThat(counterAfterFirstLikeAttempt, Matchers.equalTo(counterAfterSecondLikeAttempt));
    }
}