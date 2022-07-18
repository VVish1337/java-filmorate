package ru.yandex.practicum.filmorate.storage.user;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptsForTests.sql")
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage;

    private User getTestUser() {
        return new User(1L,"test@mail.ru",
                "testUser",
                "testusername",new Date(1999,1,1));
    }

    @Test
    void userCreate(){
        userDbStorage.create(getTestUser());
        assertThat(userDbStorage.getUserList())
                .contains(getTestUser())
                .hasSize(1);
    }

    @Test
    void userGetById(){
        userDbStorage.create(getTestUser());
        assertThat(userDbStorage.getUserById(1)).isEqualTo(getTestUser());
    }

    @Test
    void userGetList(){
        userDbStorage.create(getTestUser());
        userDbStorage.create(getTestUser());
        assertThat(userDbStorage.getUserList())
                .hasSize(2);
    }

    @Test
    void updateUser(){
        User user = userDbStorage.create(getTestUser());
        user.setName("(upd)name");
        userDbStorage.update(user);
        assertThat(userDbStorage.getUserById(1))
                .hasFieldOrPropertyWithValue("name", "(upd)name");
    }

    @Test
    void addAndGetFriend(){
        User user = userDbStorage.create(getTestUser());
        User user1 = userDbStorage.create(getTestUser());
        userDbStorage.addFriend(user.getId(),user1.getId());
        assertThat(userDbStorage.getUserFriends(user.getId())).contains(user1).hasSize(1);
    }

    @Test
    void removeFriend() {
        User user = userDbStorage.create(getTestUser());
        User user1 = userDbStorage.create(getTestUser());
        userDbStorage.addFriend(user.getId(),user1.getId());
        userDbStorage.deleteFriend(user.getId(),user1.getId());
        assertThat(userDbStorage.getUserFriends(user.getId())).isEmpty();
    }
}
