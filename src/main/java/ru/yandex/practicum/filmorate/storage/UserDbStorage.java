package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static User makeUser(ResultSet resultSet,int rowNum) throws SQLException {
        return new User(resultSet.getLong("USER_ID"),
                resultSet.getString("EMAIl"),
                resultSet.getString("LOGIN"),
                resultSet.getString("USER_NAME"),
                resultSet.getDate("BIRTHDAY").toLocalDate()
                );
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS(EMAIL, LOGIN, USER_NAME,BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            }else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getUserById(long userId) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID=?";
        final List<User> users = jdbcTemplate.query(sqlQuery,UserDbStorage::makeUser,userId);
        if(users.size() !=0){
            //TODO not found
        }
        return users.get(0);
    }

}
