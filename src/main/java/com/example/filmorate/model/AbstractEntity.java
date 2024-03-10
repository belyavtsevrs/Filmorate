package com.example.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractEntity {
    protected Long id;

    public AbstractEntity(Long id) {
        this.id = id;
    }
    public AbstractEntity(){

    }
    public Long getId() {
        return id;
    }
}
