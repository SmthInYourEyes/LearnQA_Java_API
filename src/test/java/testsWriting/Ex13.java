package testsWriting;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;


public class Ex13 {
    @ParameterizedTest
    @ValueSource(strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"})
    public void userAgent(String userAgent) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("User-Agent", userAgent);

        //
        JsonPath response = RestAssured
                .given()
                .header("User-Agent",queryParams)
                .get(" https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        String platform = response.get("platform");
        String browser = response.get("browser");
        String device = response.get("device");

        System.out.println("Platform: " + platform);
        System.out.println("Browser: " + browser);
        System.out.println("Device: " + device);

    }

}