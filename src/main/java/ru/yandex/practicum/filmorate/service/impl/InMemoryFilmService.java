package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    private final InMemoryFilmStorage storage;

    @Override
    public List<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    @Override
    public void addFilm(Film film) {
        storage.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        storage.updateFilm(film);
    }
}
