package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        return namedParameterJdbcTemplate.query("SELECT * FROM genre", new GenreMapper());
    }

    @Override
    public List<Genre> getGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());
            String sqlTestGenreId = "SELECT * FROM genre WHERE id IN (:genreIds)";
            Map<String, Object> params = new HashMap<>();
            params.put("genreIds", genreIds);
            List<Genre> genres = namedParameterJdbcTemplate.query(sqlTestGenreId, params, new GenreMapper());
            if (genres.isEmpty()) {
                throw new NotFoundException("Жанры с id " + genreIds + " не найдены");
            }
            return genres;
        } else return new ArrayList<>();
    }

    @Override
    public Genre getGenre(int id) {
        String genreTableQuery = "SELECT * FROM genre WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Genre> genre = namedParameterJdbcTemplate.query(genreTableQuery, params, new GenreMapper());
        if (genre.isEmpty()) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
        return genre.getFirst();
    }

    @Override
    public Map<Long, List<Genre>> getAllGenresByFilms() {
        String sql = "SELECT * FROM film_genre fg JOIN genre g ON fg.genre_id = g.id";
        Map<Long, List<Genre>> allGenresByFilms = new HashMap<>();
        namedParameterJdbcTemplate.query(sql, rs -> {
            Long filmId = rs.getLong("film_id");
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            allGenresByFilms.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
        });
        return allGenresByFilms;
    }
}
