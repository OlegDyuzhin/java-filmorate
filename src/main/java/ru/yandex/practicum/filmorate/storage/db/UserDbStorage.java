package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id", new UserMapper());
    }

    @Override
    public User createUser(User user) {
        String userTableQuery = "INSERT INTO users (name, email, login, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(userTableQuery, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public void updateUser(User user) {
        validateUser(user.getId());
        String userTableQuery = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(
                userTableQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        log.info("Пользователь обновлен: {}", user.getLogin());
    }

    @Override
    public User getUserToId(Long userId) {
        String userTableQuery = "SELECT * FROM users WHERE id = ?";
        List<User> user = jdbcTemplate.query(userTableQuery, new UserMapper(), userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return user.getFirst();
    }

    @Override
    public void addFriends(Long idUser1, Long idUser2) {
        validateUser(idUser1);
        validateUser(idUser2);
        String sql = "INSERT INTO friendship (user_id1, user_id2) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUser1, idUser2);
    }

    @Override
    public void removeFriend(Long idUser1, Long idUser2) {
        validateUser(idUser1);
        validateUser(idUser2);
        String sql = "DELETE FROM friendship WHERE user_id1 = ? AND user_id2 = ?";
        jdbcTemplate.update(sql, idUser1, idUser2);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        validateUser(id);
        String sql = "SELECT u.* FROM friendship f " +
                "JOIN users u ON f.user_id2 = u.id " +
                "WHERE f.user_id1 = ?";
        return jdbcTemplate.query(sql, new UserMapper(), id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        validateUser(id);
        validateUser(otherId);
        String sql = "SELECT u.* FROM friendship f1 " +
                "JOIN friendship f2 ON f1.user_id2 = f2.user_id2 " +
                "JOIN users u ON f1.user_id2 = u.id " +
                "WHERE f1.user_id1 = ? AND f2.user_id1 = ?";
        return jdbcTemplate.query(sql, new UserMapper(), id, otherId);
    }

    private boolean userExists(Long userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    private void validateUser(Long userId) {
        if (!userExists(userId)) {
            throw new NotFoundException("Польхователя с id" + userId + " не существует");
        }
    }
}