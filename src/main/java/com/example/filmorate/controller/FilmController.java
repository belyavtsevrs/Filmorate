package com.example.filmorate.controller;

import com.example.filmorate.model.Film;
import com.example.filmorate.model.User;
import com.example.filmorate.service.FilmService;
import com.example.filmorate.storage.Repository.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/find-by-title")
    public void popularFilms(@RequestParam("title")String title){
        filmService.findByTitle(title);
    }

    @DeleteMapping("/{userId}/like/{filmId}")
    public void deleteLike(@PathVariable("userId")Long userId,@PathVariable("filmId") Long filmId){
        filmService.deleteLike(filmId,userId);
    }

    @PutMapping("/{userId}/like/{filmId}")
    public void setLike(@PathVariable("userId")Long userId,@PathVariable("filmId")Long filmId){
        filmService.setLike(filmId,userId);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable("id")Long id){
        filmService.update(filmService.findById(id));
    }

    @GetMapping("/find-all")
    public List<Film> findAll(){
        return filmService.findAll();
    }

    @PostMapping("/create-film")
    public void createFilm(@RequestBody Film film){
        filmService.create(film);
    }

    @DeleteMapping("/delete")
    public void deleteFilm(@RequestParam Long id){
        filmService.delete(id);
    }

    @GetMapping("/find-by-id/{id}")
    public Film findById(@PathVariable("id") Long id){
        return filmService.findById(id);
    }
}
