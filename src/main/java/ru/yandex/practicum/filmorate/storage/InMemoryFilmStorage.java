package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryFilmStorage {
    private final Map<Long, Film> filmStorage = new HashMap<>();
    private long id = 0L;

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    public void addFilm(Film film) {
        if (filmStorage.containsKey(film.getId())) {
            throw new DuplicateException(String.format("Фильм с id: %d уже существует", film.getId()));
        } else {
            film.setId(++id);
            filmStorage.put(id, film);
        }
    }

    public void updateFilm(Film film) {
        if (filmStorage.containsKey(film.getId())) {
            filmStorage.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException("Обновляемого фильма не найдено");
        }
    }

}
