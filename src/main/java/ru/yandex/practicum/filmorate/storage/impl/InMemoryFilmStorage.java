package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmStorage = new HashMap<>();
    private long id = 0L;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public void addFilm(Film film) {
        if (filmStorage.containsKey(film.getId())) {
            throw new DuplicateException(String.format("Фильм с id: %d уже существует", film.getId()));
        } else {
            film.setId(++id);
            filmStorage.put(id, film);
        }
    }

    @Override
    public void updateFilm(Film film) {
        if (filmStorage.containsKey(film.getId())) {
            filmStorage.put(film.getId(), film);
        } else {
            throw new NotFoundException("Обновляемого фильма не найдено");
        }
    }

    @Override
    public Film getFilmToId(Long filmId) {
        if (filmStorage.get(filmId) == null) throw new NotFoundException("Фильма с id: " + filmId + " нет");
        return filmStorage.get(filmId);
    }
}
