package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Author;
import com.haulmont.testtask.entity.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {

    void createBook(Book book);
    void updateBook(Book book);
    void deleteBook(Book book) throws SQLException;
    List<Book> getAllBooks();
    List<Book> filterByName(String value);
    List<Book> filterByAuthor(Author value);
    List<Book> filterByPublisher(Book.Publisher value);
}
