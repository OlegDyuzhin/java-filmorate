package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User createUser(User user);

    void updateUser(User user);

    User getUserToId(Long userId);

    void addFriends(Long idUser1, Long idUser2);

    void removeFriend(Long idUser1, Long idUser2);

    Collection<User> getFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);
}
