package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
  private final MpaDbStorage mpaDbStorage;

  @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public MPA getById(long id){
      return mpaDbStorage.getById(id);
    }

    public List<MPA> getMpaList() {
      return mpaDbStorage.getMpaList();
    }
}
