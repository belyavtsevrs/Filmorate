package com.example.filmorate.service;

import com.example.filmorate.model.AbstractEntity;

import java.util.List;

public interface CommonService<E extends AbstractEntity> {
    E create(E data);
    E update(E data);
    E findById(Long id);
    List<E> findAll();
    void delete(Long id);
    void validateBeforeCreate(E data);
    void validateBeforeUpdate(E data);
    void validId(Long id);

}
