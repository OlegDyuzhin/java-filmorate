package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InMemoryMpaService implements MpaService {

    private final MpaStorage storage;

    @Override
    public List<Mpa> getAllMpa() {
        return storage.getAllMpa();
    }

    @Override
    public Mpa getMpa(int id) {
        return storage.getMpa(id);
    }
}