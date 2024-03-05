package com.example.filmorate.storage.Repository;


import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@AllArgsConstructor
@Component
public class UserRepository implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public User create(User data) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        Map<String,Object> values = new HashMap<>();
        values.put("EMAIL",data.getEmail());
        values.put("LOGIN",data.getLogin());
        values.put("NAME",data.getName());
        values.put("BIRTHDAY",data.getBirthday());
        data.setId(insert.executeAndReturnKey(values).longValue());
        return data;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql,this::mapToUser);
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT* FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql,this::mapToUser,id).stream().findFirst().orElse(null);
    }

    @Override
    public User update(User data) {
        String sql = "UPDATE USERS SET EMAIL = ? , LOGIN = ? , NAME = ? ,BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sql,data.getEmail(),data.getLogin(),data.getName(),data.getBirthday(),data.getId());
        return data;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql,id);
    }

    @Override
    public boolean containsEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = ?";
        return jdbcTemplate.query(sql,this::mapToUser,email).isEmpty();
    }
    @Override
    public void addFriend(Long id , Long userId){
        insertFriend(id,userId);
        insertFriend(userId,id);
    }
    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID1 = ? AND USER_ID2 = ?";
        jdbcTemplate.update(sql,id,friendId);
    }

    @Override
    public List<User> loadFriends(Long id) {
        String sql = "SELECT" +
                            " * "+
                        "FROM " +
                            "USERS " +
                        "JOIN " +
                            "FRIENDSHIP ON USER_ID = USER_ID2 " +
                        "WHERE " +
                            "USER_ID1 = ?";
        return jdbcTemplate.query(sql,this::mapToUser,id);
    }

    @Override
    public List<User> commonFriends(Long id, Long userId) {
        String sql = "SELECT USERS.* FROM USERS" +
                "   JOIN " +
                "       FRIENDSHIP F1" +
                "   ON" +
                "       USERS.USER_ID = F1.USER_ID2" +
                "   JOIN " +
                "       FRIENDSHIP F2" +
                "   ON" +
                "       F1.USER_ID2 = F2.USER_ID2" +
                "   WHERE " +
                "       F1.USER_ID1 = ? AND F2.USER_ID1 = ?";
        return jdbcTemplate.query(sql,this::mapToUser,id,userId);
    }
    public List<Long> friendsId(Long id){
        String sql = "SELECT USER_ID2 FROM FRIENDSHIP WHERE USER_ID1 = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Long.class),id);
    }

    private void insertFriend(Long id, Long userId) {
        String sql = "INSERT INTO FRIENDSHIP (USER_ID1 , USER_ID2 , CONFIRMED) VALUES (?,?,?)";
        jdbcTemplate.update(sql,id,userId,true);
    }

    private User mapToUser(ResultSet resultSet,int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("USER_ID"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setName(resultSet.getString("NAME"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());
        return user;
    }

}
