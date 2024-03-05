package com.example.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@ToString
public class User extends AbstractEntity{
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 5 , max = 20)
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends;
    public User(){}
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}
