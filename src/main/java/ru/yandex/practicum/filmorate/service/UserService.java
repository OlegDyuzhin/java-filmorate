package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User createUser(User user);

    void updateUser(User user);

    void addFriends(Long userId, Long friendsId);

    void deleteFriend(Long userId, Long friendsId);

    List<User> getAllFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long commonUserId);

    User getUserOnId(Long userId);
}
