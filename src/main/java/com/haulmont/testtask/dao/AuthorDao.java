package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Author;

import java.sql.SQLException;
import java.util.List;

public interface AuthorDao {
    void createAuthor(Author author);
    void updateAuthor(Author author);
    void deleteAuthor(Author author) throws SQLException;
    List<Author> getAllAuthors();
}
