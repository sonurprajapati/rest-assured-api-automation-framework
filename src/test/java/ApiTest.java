import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.*;

public class ApiTest {

        @Test(priority = 1, enabled = false)
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

        @Test(priority = 2, enabled = false)
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

        @Test(priority = 3, enabled = false)
        public void userNotFound() {

                Response response = RestAssured
                                .given()
                                .when()
                                .get("https://reqres.in/api/users/23");

                System.out.println(response.asPrettyString());

                response.then()
                                .statusCode(404);
        }

        @Test(priority = 4, enabled = false)
        public void createUser() {

                String requestBody = """
                                {
                                    "name": "morpheus",
                                    "job": "leader"
                                }
                                """;

                Response response = RestAssured
                                .given()
                                .header("Content-Type", "application/json")
                                .body(requestBody)
                                .when()
                                .post("https://reqres.in/api/users");

                long resSize = response.asByteArray().length;
                System.out.println(resSize);
                assertTrue(resSize > 0);

                // System.out.println(response.prettyPrint());

                String name = response.path("support.url");
                System.out.println(name);
                assertEquals(name, "https://reqres.in/go/posthog");

                long resCode = response.statusCode();
                System.out.println(resCode);
                // assertEquals(resCode, 201);
                assertTrue(resCode > 200);

                response.then()
                                .statusCode(201)
                                .body("name", org.hamcrest.Matchers.equalTo("morpheus"))
                                .body("job", org.hamcrest.Matchers.equalTo("leader"));
        }

        @Test(priority = 5, enabled = true)
        public void testAPI() {

                Response response = given()
                                .when()
                                .get("https://reqres.in/api/users?page=2");

                // System.out.println(response.prettyPrint());

                String michaelEmail = response.path("data[0].email");
                assertEquals(michaelEmail, "michael.lawson@reqres.in");

                long testApiResCode = response.statusCode();
                assertTrue(testApiResCode>=200);

                String testApiStatus = response.statusLine();
                // System.out.println(testApiStatus);
                assertTrue(testApiStatus.contains("OK"));



                System.out.println(michaelEmail);

        }
}