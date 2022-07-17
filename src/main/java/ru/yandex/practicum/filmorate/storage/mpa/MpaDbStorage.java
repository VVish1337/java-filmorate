package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage{

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, MpaDbStorage::makeMpa, id)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("MPA with "+id+" not found"));
    }

    static Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getLong("MPA_ID"),resultSet.getString("MPA_NAME"));
    }

    @Override
    public List<Mpa> getMpaList() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, MpaDbStorage::makeMpa);
    }
}
