package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete user cases")
@Feature("Delete")
public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("Тест проверяет невозможность удаления пользоваетля с ID = 2")
    @DisplayName("Тест проверяет невозможность удаления пользоваетля с ID = 2")
    @Owner("Shustov Evgenii")
    @TmsLink("TMS-12347")
    @Issue("DEL-12347")
    @Test
    public void testDeleteOldUserWithoutAuth() {
        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");


        //Удаляем пользователя с ID = 2
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/2",
                header,
                token
        );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser, "error","Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }


    @Description("Тест проверяет удаление авторизированного пользователя")
    @DisplayName("Тест проверяет удаление авторизированного пользователя")
    @Owner("Shustov Evgenii")
    @TmsLink("TMS-12347")
    @Issue("DEL-12347")
    @Test
    public void testDeleteNewUserWithAuth() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");


        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");


        //Удаляем
        Response responseDeleteUser = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                header,
                token
        );


        //Получаем данные пользователя
        Response responseUserData = apiCoreRequests.
                makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        token);

        Assertions.assertJsonByName(responseDeleteUser, "success", "!");
        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }


    @Description("Тест проверяет удаление другого пользователя")
    @DisplayName("Тест проверяет удаление другого пользователя")
    @Test
    @Owner("Shustov Evgenii")
    @TmsLink("TMS-12348")
    @Issue("DEL-12348")
    public void testDeleteNewUserWithAnotherAuth() {
        //Создаем пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/", userData).jsonPath();

        String userId = responseCreateAuth.getString("id");


        //Авторизируемся
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login/", authData);

        String header = responseGetAuth.getHeader("x-csrf-token");
        String token = responseGetAuth.getCookie("auth_sid");


        //Удаляем
        Response responseDeleteUser = apiCoreRequests.
                makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        token
                );


        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertJsonByName(responseDeleteUser,"error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }
}
