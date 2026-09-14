import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ApiTest {

    @Test
    public void getUsers() {

        Response response = RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users?page=2");

        System.out.println(response.asPrettyString());

        response.then()
                .statusCode(200)
                .body("page", org.hamcrest.Matchers.equalTo(2))
                .body("per_page", org.hamcrest.Matchers.equalTo(6))
                .body("total", org.hamcrest.Matchers.equalTo(12))
                .body("total_pages", org.hamcrest.Matchers.equalTo(2))
                .body("data.size()", org.hamcrest.Matchers.equalTo(6))
                .body(matchesJsonSchemaInClasspath("schema/users-schema.json"));
    }
    
    @Test
    public void getSingleUser() {

        Response response = RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/2");

        System.out.println(response.asPrettyString());

        response.then()
                .statusCode(200)
                .body("data.id", org.hamcrest.Matchers.equalTo(2))
                .body("data.email", org.hamcrest.Matchers.equalTo("janet.weaver@reqres.in"))
                .body("data.first_name", org.hamcrest.Matchers.equalTo("Janet"))
                .body("data.last_name", org.hamcrest.Matchers.equalTo("Weaver"));
    }
}