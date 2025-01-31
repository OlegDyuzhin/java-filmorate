package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
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
    public void createUser(User user) {
        storage.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        storage.updateUser(user);
    }

    @Override
    public void addFriends(Long userId, Long friendsId) {
        storage.getUserToId(userId).getFriendsId().add(friendsId);
        storage.getUserToId(friendsId).getFriendsId().add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendsId) {
        storage.getUserToId(userId).getFriendsId().remove(friendsId);
        storage.getUserToId(friendsId).getFriendsId().remove(userId);
    }

    @Override
    public List<User> getAllFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : storage.getUserToId(userId).getFriendsId()) {
            friends.add(storage.getUserToId(friendId));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long commonUserId) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : storage.getUserToId(userId).getFriendsId()) {
            if (storage.getUserToId(commonUserId).getFriendsId().contains(friendId)) {
                friends.add(storage.getUserToId(friendId));
            }
        }
        return friends;
    }

    @Override
    public User getUserOnId(Long userId) {
        return storage.getUserToId(userId);
    }
}
