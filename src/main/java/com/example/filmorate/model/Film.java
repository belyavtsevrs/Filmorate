package com.example.filmorate.model;
 ;
 import jakarta.validation.constraints.*;
 import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;


@Setter
@Getter
@ToString
public class Film extends AbstractEntity{
    @NotBlank
    private String title;
    @Max(200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private float duration;
    @NotNull
    private Set<String> genres;
    @NotNull
    private String mpa;
    private Long like;
    private Set<Long> liked;
    public Film(Long id,String title,String description,LocalDate releaseDate,float duration){
        super(id);
        this.title=title;
        this.description=description;
        this.releaseDate=releaseDate;
        this.duration=duration;
        this.like = 0l;
        this.liked = null;
    }
    public Film(){

    }
    public boolean after1985() {
        return releaseDate.isAfter(LocalDate.of(1985, 12, 28));
    }
}
