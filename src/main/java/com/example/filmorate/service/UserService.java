package com.example.filmorate.service;

import com.example.filmorate.exceptions.AlreadyExistsException;
import com.example.filmorate.exceptions.ValidationException;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService<User, UserRepository>{

    public UserService(UserRepository storage) {
        super(storage);
    }

    public void addFriend(Long id, Long friendId){
        storage.addFriend(id,friendId);
    }

    public void deleteFriend(Long id,Long friendId){
        storage.deleteFriend(id,friendId);
    }

    public List<User> friendList(Long id){
        return storage.loadFriends(id);
    }

    public List<User> commonFriends(Long id , Long friendId){
        return storage.commonFriends(id,friendId);
    }
    @Override
    public User create(User data) {
        validateBeforeCreate(data);
        log.info("User = {} created",data);
        return super.create(data);
    }

    @Override
    public User update(User data) {
        validateBeforeUpdate(data);
        log.info("User = {} updated",data);
        return super.update(data);
    }

    @Override
    public User findById(Long id) {
         return super.findById(id);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public void validateBeforeUpdate(User data) {
        if(data.equals(null)){
            throw new ValidationException("Invalid data");
        }
        if (data.getName().isBlank()) {
            data.setName(data.getEmail());
        }
        super.validateBeforeUpdate(data);
    }

    @Override
    public void validId(Long id) {
        super.validId(id);
    }

    @Override
    public void validateBeforeCreate(User data) {
        if(data.getName().equals(null) || data.getName().isBlank()){
            data.setName(data.getEmail());
        }
        if(!storage.containsEmail(data.getEmail())){
            throw new AlreadyExistsException(String.format("User with email '%s' already exits",data.getEmail()));
        }
    }
}
