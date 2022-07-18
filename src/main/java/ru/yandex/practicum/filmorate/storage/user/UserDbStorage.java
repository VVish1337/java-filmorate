package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getLong("USER_ID"),
                resultSet.getString("EMAIl"),
                resultSet.getString("USER_NAME"),
                resultSet.getString("LOGIN"),
                resultSet.getDate("BIRTHDAY")
        );
    }

    @Override
    public List<User> getUserList() {
        String sqlQuery = ("SELECT * FROM users");
        return jdbcTemplate.query(sqlQuery,UserDbStorage::makeUser);
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS(email, user_name, login,birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            final Date birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, birthday);
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "MERGE INTO users (user_id, email, user_name, login, birthday) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public User getUserById(long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser, userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("User with this id:"+userId+" not found"));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        final String sqlQuery = "INSERT INTO friend_list (user_id,friend_id) VALUES(?,?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        final String sqlQuery = "DELETE FROM friend_list WHERE user_id = ? AND friend_id =?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getUserFriends(long userId){
        final String sqlQuery = "SELECT friend_id FROM friend_list WHERE user_id = ?";
        List<Long> friends = jdbcTemplate.queryForList(sqlQuery,Long.class,userId);
        return friends.stream()
                    .map(this::getUserById)
                    .collect(Collectors.toList());
    }
}