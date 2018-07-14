package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.entity.Author;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.List;

public class AuthorDaoImpl implements AuthorDao {

    private static AuthorDao instance;

    private EntityManager em;

    private AuthorDaoImpl(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsqldb-file-persistence-unit");
        em = emf.createEntityManager();
    }

    public static AuthorDao getInstance(){
        if (instance == null){
            instance = new AuthorDaoImpl();
        }
        return instance;
    }

    @Override
    public void createAuthor(Author author) {
        try{
            em.getTransaction().begin();
            em.persist(author);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public void updateAuthor(Author author) {
        try{
            em.getTransaction().begin();
            em.merge(author);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public void deleteAuthor(Author author) throws SQLException {
        try{
            em.getTransaction().begin();
            em.remove(author);
            em.getTransaction().commit();
        } catch (RollbackException e){
            throw new SQLException();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        TypedQuery<Author> query = em.createNamedQuery("AUTHORS.getAll", Author.class);
        return query.getResultList();
    }
}
