package com.example.filmorate.storage.Repository;


import com.example.filmorate.model.Film;
import com.example.filmorate.storage.FilmStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class FilmRepository implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Film> popularFilms(){
        String sql =  "SELECT FILM.* " +
                     "FROM FILM " +
                     "JOIN LIKED ON FILM.FILM_ID = LIKED.FILM_ID " +
                     "GROUP BY LIKED.FILM_ID " +
                     "ORDER BY LIKED.FILM_ID DESC ";
        return jdbcTemplate.query(sql,this::mapToFilm);
    }

    public void like(Long filmId,Long userId){
        String sql = "INSERT INTO LIKED (FILM_ID,USER_ID) VALUES (?,?)";
        jdbcTemplate.update(sql,filmId,userId);
    }

    public void deleteLike(Long filmId , Long userId){
        String sql = "DELETE FROM LIKED WHERE USER_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sql,filmId,userId);
    }

    public Film findByTitle(String title){
        String sql = "SELECT * FROM FILM WHERE TITLE = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Film.class),title).stream().findFirst().orElse(null);
    }
    @Transactional
    @Override
    public Film create(Film data) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.withTableName("FILM")
                .usingGeneratedKeyColumns("FILM_ID");
        Map<String,Object> values = new HashMap<>();
        values.put("TITLE",data.getTitle());
        values.put("DESCRIPTION",data.getDescription());
        values.put("RELEASE_DATE",data.getReleaseDate());
        values.put("DURATION",data.getDuration());
        data.setId(insert.executeAndReturnKey(values).longValue());
        DataSet(data);
        log.info("data = {}",data);
        return data;
    }

    private void DataSet(Film film){
        String sql = "INSERT INTO FILM_GENRES (FILM_ID,GENRE_ID) " +
                "VALUES (?,(SELECT GENRE_ID FROM GENRES WHERE GENRE = ?))";
        if(film.getGenres().size() > 0) {
            for (var x : film.getGenres()) {
                jdbcTemplate.update(sql, film.getId(), x.toString());
            }
        }
        String setRating = "INSERT INTO FILM_RATING (FILM_ID,RATING_ID)" +
                "VALUES (?,(SELECT RATING_ID FROM RATING WHERE TITLE = ?))";
        jdbcTemplate.update(setRating,film.getId(),film.getMpa());
    }
    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM FILM";
        return jdbcTemplate.query(sql,this::mapToFilm);
    }

    @Override
    public Film findById(Long id) {
        String sql = "SELECT * FROM FILM WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql,this::mapToFilm,id).stream().findFirst().orElse(null);
    }

    @Override
     public Film update(Film data) {
        String sql =  "UPDATE FILM SET TITLE = ? , DESCRIPTION = ? ,RELEASE_DATE = ? ,DURATION = ? WHERE FILM_ID = ?";
        jdbcTemplate.query(sql,this::mapToFilm, data.getTitle(),data.getDescription(),data.getReleaseDate(),data.getDuration(),data.getId());
        String updGenres = "UPDATE FILM_GENRE " +
                                  "SET GENRE_ID = (SELECT GENRE_ID FORM GENRE WHERE GENRE = ?)" +
                            "WHERE FILM_ID ="+data.getId();
        for(var x : data.getGenres()){
            jdbcTemplate.update(updGenres,x);
        }
        String updRating = "UPDATE FILM_RATING SET RATING_ID = (SELECT RATING_ID FROM RATING WHERE TITLE = "+data.getMpa()+")" +
                "WHERE FILM_ID ="+data.getId();
        jdbcTemplate.update(updRating);
        return data;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM FILM WHERE FILM_ID = "+id;
        jdbcTemplate.execute(sql);
    }
    public Film mapToFilm(ResultSet resultSet,int rowNum) throws SQLException{
        Film film = new Film();
        film.setId(resultSet.getLong("FILM_ID"));
        film.setTitle(resultSet.getString("TITLE"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(resultSet.getFloat("DURATION"));
        film.setGenres(genres(film));
        film.setMpa(rating(film).stream().findFirst().orElse(null));
        film.setLiked(new HashSet<>(liked(film)));
        film.setLike(likes(film));
        return film;
    }

    private Set<String> genres(Film film){
        String sql = "SELECT GENRE FROM GENRES JOIN FILM_GENRES ON GENRES.GENRE_ID = FILM_GENRES.GENRE_ID WHERE FILM_GENRES.FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql,String.class,film.getId()));
    }
    private Set<String> rating(Film film){
        String sql = "SELECT TITLE FROM RATING JOIN FILM_RATING ON RATING.RATING_ID = FILM_RATING.RATING_ID WHERE FILM_RATING.FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql,String.class,film.getId()));
    }
    private Set<Long> liked(Film film){
        String sql = "SELECT USER_ID FROM LIKED WHERE FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql,Long.class,film.getId()));
    }
    private Long likes(Film film){
        String sql = "SELECT COUNT(USER_ID) FROM FILM JOIN LIKED ON FILM.FILM_ID = LIKED.FILM_ID WHERE FILM.FILM_ID = ?";
        return jdbcTemplate.queryForObject(sql,Long.class,film.getId());
    }


}
