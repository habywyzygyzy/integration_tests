package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class CreateUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    private static final String HEADER_1 = "Content-Type";
    private static final String HEADER_2 = "application/json;charset=UTF-8";

    @Test
    public void postFormWithMalformedRequestDataReturnsBadRequest() {
        JSONObject jsonObj = new JSONObject().put("email", "tracy@domain.com");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .body(jsonObj.toString())
                .expect().log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .when().post(USER_API);
    }

    @Test
    public void postFormWithNonUniqueEmailInRequestDataReturnsConflict() {
        JSONObject jsonObj = new JSONObject().put("email", "john@domain.com");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header(HEADER_1, HEADER_2)
                .body(jsonObj.toString())
                .expect().log().all()
                .statusCode(HttpStatus.SC_CONFLICT)
                .when().post(USER_API);
    }
}
