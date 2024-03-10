package com.example.filmorate.storage;

import com.example.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends CommonStorage<Film>{

    @Override
    Film create(Film data);

    @Override
    List<Film> findAll();

    @Override
    Film findById(Long id);

    @Override
    Film update(Film data);

    @Override
    void delete(Long id);
}
