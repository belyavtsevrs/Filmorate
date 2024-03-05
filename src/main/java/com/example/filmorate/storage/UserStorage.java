package com.example.filmorate.storage;

import com.example.filmorate.model.User;

import java.util.List;

public interface UserStorage extends CommonStorage<User>{

    boolean containsEmail(String email);
    void addFriend(Long id, Long userId);
    void deleteFriend(Long id,Long userId);
    List<User> loadFriends(Long id);
    /*void updateFriend();
    boolean containsFriendship(Long id1 , Long id2 , Boolean confirmed);*/
    List<User> commonFriends(Long id , Long userId);

}
