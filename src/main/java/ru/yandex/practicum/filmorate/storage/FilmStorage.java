package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms(//Map<Long, List<Genre>> allGenresByFilms
    );

    Film addFilm(Film film);

    void updateFilm(Film film);

    Film getFilmToId(Long filmId);

    List<Film> getPopularFilmOnLike(int count);
}
