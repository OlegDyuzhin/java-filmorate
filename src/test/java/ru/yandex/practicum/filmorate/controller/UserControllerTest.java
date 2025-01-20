package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest extends BaseControllerTest {

    private final String path = "/users";
    @Autowired
    private ObjectMapper objectMapper;

    private User setDefaultUser() {
        User user = new User();
        user.setName("Name");
        user.setEmail("User@bk.ru");
        user.setBirthday(LocalDate.of(2000, 10, 10));
        user.setLogin("login");
        return user;
    }


    @DisplayName("Обычный GET")
    @Test
    void getAllUsers() throws IOException, InterruptedException {
        User user = setDefaultUser();
        String userJson = objectMapper.writeValueAsString(user);
        getResponse(path, RequestMethod.POST, userJson);
        httpResponse = getResponse(path, RequestMethod.GET, userJson);
        user.setId(1L);
        List<User> expectedUsers = List.of(user);
        String expectedJson = objectMapper.writeValueAsString(expectedUsers);
        assertEquals(expectedJson, httpResponse.body());
    }

    @DisplayName("Обычное добавление")
    @Test
    void addUserTest() throws IOException, InterruptedException {
        User user = setDefaultUser();
        String userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        user.setId(1L);
        String expectedJson = objectMapper.writeValueAsString(user);
        assertEquals(expectedJson, httpResponse.body());
    }

    @DisplayName("Обычное обновление")
    @Test
    void updateUserTest() throws Exception {
        User user = setDefaultUser();
        String userJson = objectMapper.writeValueAsString(user);
        getResponse(path, RequestMethod.POST, userJson);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);

        User user2 = new User();
        user2.setId(1L);
        user2.setName("Name2");
        user2.setEmail("asdsad@sdsds.ds");
        user2.setBirthday(LocalDate.of(2020, 3, 20));
        user2.setLogin("login");

        userJson = objectMapper.writeValueAsString(user2);
        httpResponse = getResponse(path, RequestMethod.PUT, userJson);

        String expectedJson = objectMapper.writeValueAsString(user2);
        assertEquals(expectedJson, httpResponse.body());
    }

    @DisplayName("Проверка валидации пользователя")
    @Test
    void addNoValidUserTest() throws IOException, InterruptedException {
        User user = setDefaultUser();
        user.setEmail("");
        String userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущен пустой Email");

        user = setDefaultUser();
        user.setEmail("sadasda");
        userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущен Email без @");

        user = setDefaultUser();
        user.setLogin("");
        userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущен пустой логин");

        user.setLogin("dfdsfsd sdfsdf");
        userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущен логин с пробелом");

        user = setDefaultUser();
        user.setBirthday(LocalDate.now().plusDays(1));
        userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущен ДР в будущем");

        httpResponse = getResponse(path, RequestMethod.GET, userJson);
        assertEquals("[]", httpResponse.body(), "Пропущен не валидный объект");

        user = setDefaultUser();
        user.setName(null);
        userJson = objectMapper.writeValueAsString(user);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        user.setName(user.getLogin());
        user.setId(1L);
        String expectedJson = objectMapper.writeValueAsString(user);

        assertEquals(expectedJson, httpResponse.body(), "Не работает подмена имени логином");
    }
}