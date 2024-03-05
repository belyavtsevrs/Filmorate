package com.example.filmorate.storage;

import com.example.filmorate.model.AbstractEntity;

import java.util.List;

public interface CommonStorage <E extends AbstractEntity> {
    E create(E data);
    List<E> findAll();
    E findById(Long id);

    E update (E data);
    void delete(Long id);
}
