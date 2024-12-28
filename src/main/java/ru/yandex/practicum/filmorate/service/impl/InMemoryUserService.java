package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final InMemoryUserStorage storage;

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public void createUser(User user) {
        storage.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        storage.updateUser(user);
    }
}
