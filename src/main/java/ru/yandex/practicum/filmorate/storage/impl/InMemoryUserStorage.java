package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private long id = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public void createUser(User user) {
        if (userStorage.containsKey(user.getId())) {
            throw new DuplicateException(String.format("Пользователь с id: %d уже существует", user.getId()));
        } else {
            user.setId(++id);
            userStorage.put(id, user);
        }
    }

    @Override
    public void updateUser(User user) {
        if (userStorage.containsKey(user.getId())) {
            userStorage.put(user.getId(), user);
        } else {
            throw new NotFoundException("Обновляемого пользователя не найдено");
        }
    }

    @Override
    public User getUserToId(Long userId) {
        if (userStorage.get(userId) == null) throw new NotFoundException("Пользователя с id: " + userId + " нет");
        return userStorage.get(userId);
    }
}
