package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmControllerTest extends BaseControllerTest {

    private final String path = "/films";
    @Autowired
    private ObjectMapper objectMapper;

    private Film setDefaultFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 10, 10));
        film.setDuration(160L);
        return film;
    }


    @DisplayName("Обычный GET")
    @Test
    void getAllFilmsTest() throws IOException, InterruptedException {
        Film film = setDefaultFilm();
        String filmJson = objectMapper.writeValueAsString(film);
        getResponse(path, RequestMethod.POST, filmJson);
        httpResponse = getResponse(path, RequestMethod.GET, filmJson);
        film.setId(1L);
        List<Film> expectedFilms = List.of(film);
        String expectedJson = objectMapper.writeValueAsString(expectedFilms);
        assertEquals(expectedJson, httpResponse.body());
    }

    @DisplayName("Обычное добавление")
    @Test
    void addFilmTest() throws IOException, InterruptedException {
        Film film = setDefaultFilm();
        String filmJson = objectMapper.writeValueAsString(film);
        httpResponse = getResponse(path, RequestMethod.POST, filmJson);
        film.setId(1L);
        String expectedJson = objectMapper.writeValueAsString(film);
        assertEquals(expectedJson, httpResponse.body());
    }

    @DisplayName("Обычное обновление")
    @Test
    void updateFilmTest() throws Exception {
        Film film = setDefaultFilm();
        String filmJson = objectMapper.writeValueAsString(film);
        getResponse(path, RequestMethod.POST, filmJson);
        httpResponse = getResponse(path, RequestMethod.POST, filmJson);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Film2");
        film2.setDescription("New Description");
        film2.setReleaseDate(LocalDate.of(1990, 2, 2));
        film2.setDuration(120L);

        filmJson = objectMapper.writeValueAsString(film2);
        httpResponse = getResponse(path, RequestMethod.PUT, filmJson);

        String expectedJson = objectMapper.writeValueAsString(film2);
        assertEquals(expectedJson, httpResponse.body());
    }

    @DisplayName("Проверка валидации пользователя")
    @Test
    void addNoValidFilmTest() throws IOException, InterruptedException {
        Film film = setDefaultFilm();
        film.setName("");
        String userJson = objectMapper.writeValueAsString(film);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущено пустое Имя");

        film = setDefaultFilm();
        film.setDescription("a".repeat(201));
        userJson = objectMapper.writeValueAsString(film);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущено длинное описание >200");

        film = setDefaultFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        userJson = objectMapper.writeValueAsString(film);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущена дата ранее заданной");

        film = setDefaultFilm();
        film.setDuration(-1L);
        userJson = objectMapper.writeValueAsString(film);
        httpResponse = getResponse(path, RequestMethod.POST, userJson);
        assertEquals(400, httpResponse.statusCode(), "Пропущена отрицательная продолжительность");

        httpResponse = getResponse(path, RequestMethod.GET, userJson);
        assertEquals("[]", httpResponse.body(), "Пропущен не валидный объект");
    }
}