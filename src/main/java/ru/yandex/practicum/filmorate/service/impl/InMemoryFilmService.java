package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {


    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final GenreService genreService;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        film.setGenres(genreStorage.getGenres(film));
        film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        return filmStorage.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        film.setGenres(genreStorage.getGenres(film));
        film.setMpa(mpaStorage.getMpa(film.getMpa().getId()));
        filmStorage.updateFilm(film);
    }

    @Override
    public void addLikeFilm(Long filmId, Long userId) {
        likeStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLikeFilm(Long filmId, Long userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilmOnLike(Integer countFilms) {
        return filmStorage.getPopularFilmOnLike(countFilms);
    }

    @Override
    public Film getFilmOnId(Long filmId) {
        return filmStorage.getFilmToId(filmId);
    }
}
