package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.dao.BookDao;
import com.haulmont.testtask.entity.Author;
import com.haulmont.testtask.entity.Book;
import com.haulmont.testtask.entity.Genre;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.List;

public class BookDaoImpl implements BookDao {

    private static BookDao instance;

    private EntityManager em;

    private BookDaoImpl(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsqldb-file-persistence-unit");
        em = emf.createEntityManager();
    }

    public static BookDao getInstance(){
        if (instance == null){
            instance = new BookDaoImpl();
        }
        return instance;
    }

    @Override
    public void createBook(Book book) {
        try{
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public void updateBook(Book book) {
        try{
            em.getTransaction().begin();
            em.merge(book);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public void deleteBook(Book book) throws SQLException {
        try{
            em.getTransaction().begin();
            em.remove(book);
            em.getTransaction().commit();
        } catch (RollbackException e){
            throw new SQLException();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<Book> getAllBooks() {
        TypedQuery<Book> query = em.createNamedQuery("BOOK.getAll", Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> filterByName(String value) {
        List<Book> bookList = null;
        try{
            em.getTransaction().begin();
            bookList = em.createQuery("select b from Book b where b.name like:value")
                    .setParameter("value", "%" + value + "%").getResultList();
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
        return bookList;
    }

    @Override
    public List<Book> filterByAuthor(Author value) {
        List<Book> bookList = null;
        try{
            em.getTransaction().begin();
            bookList = em.createQuery("select b from Book b where b.author = :value")
                    .setParameter("value", value).getResultList();
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
        return bookList;
    }

    @Override
    public List<Book> filterByPublisher(Book.Publisher value) {
        List<Book> bookList = null;
        try{
            em.getTransaction().begin();
            bookList = em.createQuery("select b from Book b where b.publisher = :value")
                    .setParameter("value", value).getResultList();
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
        return bookList;
    }
}
