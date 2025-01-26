package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    @Override
    public void addLikeFilm(Long filmId, Long userId) {
        if (userService.getUserOnId(userId) != null) filmStorage.getFilmToId(filmId).getLikeId().add(userId);
    }

    @Override
    public void removeLikeFilm(Long filmId, Long userId) {
        if (userService.getUserOnId(userId) != null) filmStorage.getFilmToId(filmId).getLikeId().remove(userId);
    }

    @Override
    public List<Film> getPopularFilmOnLike(Integer countFilms) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getAmountLikes).reversed())
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmOnId(Long filmId) {
        return filmStorage.getFilmToId(filmId);
    }
}
