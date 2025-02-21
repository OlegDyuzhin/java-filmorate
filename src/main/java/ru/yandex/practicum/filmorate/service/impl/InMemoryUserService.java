package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserStorage storage;

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        return storage.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        storage.updateUser(user);
    }

    @Override
    public void addFriends(Long userId, Long friendsId) {
        storage.addFriends(userId, friendsId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendsId) {
        storage.removeFriend(userId, friendsId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        return List.copyOf(storage.getFriends(userId));
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long commonUserId) {
        return List.copyOf(storage.getCommonFriends(userId, commonUserId));
    }

    @Override
    public User getUserOnId(Long userId) {
        return storage.getUserToId(userId);
    }
}