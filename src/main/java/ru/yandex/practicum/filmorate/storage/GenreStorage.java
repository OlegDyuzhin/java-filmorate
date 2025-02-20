package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreStorage {

    List<Genre> getGenres();

    List<Genre> getGenres(Film film);

    Genre getGenre(int id);

    Map<Long, List<Genre>> getAllGenresByFilms();

}