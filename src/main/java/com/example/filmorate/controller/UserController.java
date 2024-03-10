package com.example.filmorate.controller;


import com.example.filmorate.model.User;
import com.example.filmorate.service.UserService;
import com.example.filmorate.storage.Repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/friends/{friend-Id}")
    public void addFriend(@PathVariable("id")Long id,@PathVariable("friend-Id") Long friendId){
        userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/delete/{friend-Id}")
    public void deleteFriend(@PathVariable("id")Long id,@PathVariable("friend-Id") Long friendId){
        userService.deleteFriend(id,friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id")Long id){
        return userService.friendList(id);
    }

    @GetMapping("/{id}/common-friends/{friend-id}")
    public List<User> getCommonFriends(@PathVariable("id")Long id, @PathVariable("friend-id")Long friendId){
        return userService.commonFriends(id,friendId);
    }

    @GetMapping("/find-all")
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/find-by-id/{id}")
    public User findById(@PathVariable("id") Long id){
        return userService.findById(id);
    }

    @PutMapping("/update-user")
    public User updateUser(@Valid @RequestBody User user){
        return userService.update(user);
    }

    @PostMapping("/create-user")
    public User createuser(@Valid @RequestBody User user){
        return userService.create(user);
    }

    @DeleteMapping("/delete-user")
    public void deleteUser(@RequestParam("id")Long id){
        userService.delete(id);
    }

}
