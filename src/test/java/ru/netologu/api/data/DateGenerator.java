package ru.netologu.api.data;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DateGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DateGenerator() {
    }


    // Делаем post запрос в параметре метода передаем зарегистрированного юзера.
    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    // Генерируем рандомное имя с помощью библиотеки faker.
    public static String getRandomLogin() {
        String login = faker.name().firstName();
        return login;
    }

    // Генерируем рандомный пароль с помощью библиотеки faker.
    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }


    public static class Registration {
        private Registration() {
        }

        // Создаем юзера
        public static RegistrationDto getUser(String status) {
            var user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        // Создаем зарегистрированного юзера
        public static RegistrationDto getRegistredUser(String status) {
            var registerdUser = getUser(status);
            sendRequest(registerdUser);
            return registerdUser;
        }
    }


    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
