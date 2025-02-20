package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Primary
@Slf4j
@Repository
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sql = """
                SELECT f.id, f.name, f.description, f.releaseDate, f.duration,
                       m.mpa_rating_id, m.mpa_name
                FROM film f
                JOIN mpa_rating m ON f.mpa_id = m.mpa_rating_id
                ORDER BY f.id;
                """;
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Пришел фильм {}" + film);
        String sql = "INSERT INTO film (name, description, releaseDate, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);

        List<Genre> genres = film.getGenres();
        String genreSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(genreSql, genres, genres.size(), (ps, genre) -> {
            ps.setLong(1, id);
            ps.setInt(2, genre.getId());
        });
        film.setGenres(genres);
        return film;
    }

    @Override
    public void updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (rows == 0) throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");

        String deleteGenresSql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());

            String genreSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(genreSql, uniqueGenres, uniqueGenres.size(), (ps, genre) -> {
                ps.setLong(1, film.getId());
                ps.setInt(2, genre.getId());
            });
        }
        log.info("Фильм обновлен: {}", film.getName());
    }

    @Override
    public Film getFilmToId(Long filmId) {
        String sql = "SELECT f.*, m.mpa_rating_id, m.mpa_name FROM film f " +
                "JOIN mpa_rating m ON f.mpa_id = m.mpa_rating_id  WHERE f.id = ?";
        Film film = jdbcTemplate.query(sql, new FilmMapper(), filmId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));
        String genreSql = "SELECT g.id, g.name FROM genre g " +
                "JOIN film_genre fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(genreSql, new GenreMapper(), filmId);
        film.setGenres(genres);
        return film;
    }

    @Override
    public List<Film> getPopularFilmOnLike(int count) {
        String sql = """
                SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, m.*, COUNT(l.user_id) AS sum \
                FROM film f \
                LEFT JOIN likes l ON f.id=l.film_id \
                JOIN mpa_rating m ON f.mpa_id = m.mpa_rating_id \
                GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id \
                ORDER BY sum DESC \
                LIMIT ?""";

        return jdbcTemplate.query(sql, new FilmMapper(), count);
    }
}