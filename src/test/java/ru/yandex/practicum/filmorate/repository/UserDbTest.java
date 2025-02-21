package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbTest {
    @Autowired
    private UserStorage userStorage;

    private User setDefaultUser() {
        User user = new User();
        user.setName("Name");
        user.setEmail("User@bk.ru");
        user.setBirthday(LocalDate.of(2000, 10, 10));
        user.setLogin("login");
        return user;
    }


    @DisplayName("Обычный GET")
    @Test
    void getAllUsers() {
        User addeduser = userStorage.createUser(setDefaultUser());
        assertThat(addeduser).isNotNull();
        assertThat(addeduser.getId()).isGreaterThan(0);
    }

    @DisplayName("Обычное добавление")
    @Test
    void addUserTest() {
        User addedUser = userStorage.createUser(setDefaultUser());
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isGreaterThan(0);
    }


    @Test
    void getUserTest() {
        User addedUser = userStorage.createUser(setDefaultUser());
        User retrievedUser = userStorage.getUserToId(addedUser.getId());
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo(setDefaultUser().getName());
    }

    @Test
    void getAllUsersTest() {
        userStorage.createUser(setDefaultUser());
        User anotherUser = new User();
        anotherUser.setName("Test User");
        anotherUser.setEmail("User2@bk.ru");
        anotherUser.setBirthday(LocalDate.of(2000, 10, 10));
        anotherUser.setLogin("login2");
        userStorage.createUser(anotherUser);
        List<User> users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(2);
    }
}