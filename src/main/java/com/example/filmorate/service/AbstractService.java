package com.example.filmorate.service;

import com.example.filmorate.exceptions.AlreadyExistsException;
import com.example.filmorate.exceptions.NotFoundException;
import com.example.filmorate.exceptions.ValidationException;
import com.example.filmorate.model.AbstractEntity;
import com.example.filmorate.storage.CommonStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public abstract class AbstractService
        <E extends AbstractEntity,T extends CommonStorage<E>>
        implements CommonService<E>{
    private final static String ERR_MESSAGE_INVALID_ID = "Invalid id %d";
    private final static String ERR_MESSAGE_NOT_FOUND = "Not found with id %d";
    private final static String ERR_MESSAGE_INVALID_DATA = "Invalid data format";

    protected final T storage;
    @Override
    public E create(E data) {
        validateBeforeCreate(data);
        return storage.create(data);
    }

    @Override
    public E update(E data) {
        validateBeforeUpdate(data);
        E newData = storage.update(data);
        if(newData == null){
            log.warn(ERR_MESSAGE_NOT_FOUND+data.getId());
            throw new NotFoundException(String.format(ERR_MESSAGE_NOT_FOUND,data.getId()));
        }
        return null;
    }

    @Override
    public E findById(Long id) {
        validId(id);
        E data = storage.findById(id);
        if(data != null){
            return data;
        }
        log.warn(ERR_MESSAGE_NOT_FOUND+id);
        throw new NotFoundException(ERR_MESSAGE_NOT_FOUND);
    }

    @Override
    public List<E> findAll() {
        return storage.findAll();
    }

    @Override
    public void delete(Long id) {
        validId(id);
        storage.delete(id);
    }

    @Override
    public abstract void validateBeforeCreate(E data);

    @Override
    public void validateBeforeUpdate(E data) {
        validId(data.getId());
    }

    @Override
    public void validId(Long id) {
        if(id < 0){
            throw new ValidationException(String.format(ERR_MESSAGE_INVALID_ID,id));
        }
        if(id == null){
            throw new NotFoundException(String.format(ERR_MESSAGE_NOT_FOUND,id));
        }
    }

}
