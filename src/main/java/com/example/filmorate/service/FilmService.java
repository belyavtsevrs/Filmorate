package com.example.filmorate.service;


import com.example.filmorate.exceptions.AlreadyExistsException;
import com.example.filmorate.exceptions.ValidationException;
import com.example.filmorate.model.Film;
import com.example.filmorate.storage.Repository.FilmRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FilmService  extends AbstractService<Film,FilmRepository>{

    public Film findByTitle(String title){
        return storage.findByTitle(title);
    }

    public List<Film> popular( ){
        return storage.popularFilms( );
    }

    public FilmService(FilmRepository storage) {
        super(storage);
    }

    public void setLike(Long filmId , Long userId){
        storage.like(filmId,userId);
    }

    public void deleteLike(Long filmId,Long userId){
        storage.deleteLike(userId,filmId);
    }

    @Override
    public Film create(Film data) {
        validateBeforeCreate(data);
        return super.create(data);
    }

    @Override
    public Film update(Film data) {
        return super.update(data);
    }

    @Override
    public Film findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<Film> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public void validateBeforeUpdate(Film data) {
        super.validateBeforeUpdate(data);
    }

    @Override
    public void validId(Long id) {
        super.validId(id);
    }

    @Override
    public void validateBeforeCreate(Film data) {
        if(!data.after1985()){
            throw new ValidationException("The release date be after December 25, 1985");
        }
        if(data == null){
            throw new ValidationException("Invalid data");
        }
        if(storage.findByTitle(data.getTitle()) != null){
            throw new AlreadyExistsException(String.format("Film with title '%s' already exists",data.getTitle()));
        }
    }
}
