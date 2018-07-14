package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Genre;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface GenreDao {

    void createGenre(Genre genre);
    void updateGenre(Genre genre);
    void deleteGenre(Genre genre) throws SQLException;
    List<Genre> getAllGenres();
    Map<Genre, Integer> getGenresAndBooks();
}
