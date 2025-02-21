package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryGenreService implements GenreService {

    private final GenreStorage storage;

    @Override
    public List<Genre> getGenres() {
        return storage.getGenres();
    }

    @Override
    public Genre getGenre(int id) {
        return storage.getGenre(id);
    }
}