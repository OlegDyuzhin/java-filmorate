package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getAllFilms();

    void addFilm(Film film);

    void updateFilm(Film film);

    void addLikeFilm(Long filmId, Long userId);

    void removeLikeFilm(Long filmId, Long userId);

    List<Film> getPopularFilmOnLike(Integer countFilms);

    Film getFilmOnId(Long filmId);
}
