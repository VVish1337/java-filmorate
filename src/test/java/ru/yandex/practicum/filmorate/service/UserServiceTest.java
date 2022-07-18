package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptsForTests.sql")
public class UserServiceTest {
    private final UserService userService;
    private User getTestUser() {
        return new User(1L,"test@mail.ru",
                "testUser",
                "testusername",new Date(1999,1,1));
    }

    @Test
    void getUserCommonFriends(){
        User user1 = userService.create(getTestUser());
        User user2 = userService.create(getTestUser());
        User friend1 = userService.create(getTestUser());
        User friend2 = userService.create(getTestUser());
        User commonFriend = userService.create(getTestUser());
        userService.addFriend(user1.getId(), friend1.getId());
        userService.addFriend(user2.getId(), friend2.getId());
        userService.addFriend(user1.getId(), commonFriend.getId());
        userService.addFriend(user2.getId(), commonFriend.getId());
        assertThat(userService.getUserCommonFriends(user1.getId(), user2.getId()))
                .hasSize(1);
        assertThat(userService.getUserCommonFriends(user1.getId(), user2.getId()))
                .isEqualTo(List.of(commonFriend));
    }
}
