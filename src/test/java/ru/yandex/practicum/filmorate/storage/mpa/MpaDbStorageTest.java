package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scriptsForTests.sql")
public class MpaDbStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    void getMpaById() {
        assertThat(mpaStorage.getById(1).getName()).isEqualTo("G");
        assertThat(mpaStorage.getById(2).getName()).isEqualTo("PG");
        assertThat(mpaStorage.getById(3).getName()).isEqualTo("PG-13");
        assertThat(mpaStorage.getById(4).getName()).isEqualTo("R");
        assertThat(mpaStorage.getById(5).getName()).isEqualTo("NC-17");
    }

    @Test
    void getMpaList() {
        List<Mpa> mpaList = new ArrayList<>();
        mpaList.add(mpaStorage.getById(1));
        mpaList.add(mpaStorage.getById(2));
        mpaList.add(mpaStorage.getById(3));
        mpaList.add(mpaStorage.getById(4));
        mpaList.add(mpaStorage.getById(5));
        assertThat(mpaStorage.getMpaList()).isEqualTo(mpaList);
    }
}
