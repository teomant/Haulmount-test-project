package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.AuthorDao;
import com.haulmont.testtask.dao.GenreDao;
import com.haulmont.testtask.entity.Author;
import com.haulmont.testtask.entity.Genre;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreDaoImpl implements GenreDao {

    private static GenreDao instance;

    private EntityManager em;

    private GenreDaoImpl(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsqldb-file-persistence-unit");
        em = emf.createEntityManager();
    }

    public static GenreDao getInstance(){
        if (instance == null){
            instance = new GenreDaoImpl();
        }
        return instance;
    }

    @Override
    public void createGenre(Genre genre) {
        try{
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public void updateGenre(Genre genre) {
        try{
            em.getTransaction().begin();
            em.merge(genre);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public void deleteGenre(Genre genre) throws SQLException {
        try{
            em.getTransaction().begin();
            em.remove(genre);
            em.getTransaction().commit();
        } catch (RollbackException e){
            throw new SQLException();
        }catch (Exception e){
            em.getTransaction().rollback();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        TypedQuery<Genre> query = em.createNamedQuery("GENRE.getAll", Genre.class);
        return query.getResultList();
    }

    @Override
    public Map<Genre, Integer> getGenresAndBooks() {

        Map<Genre, Integer> resultMap = new HashMap<>();
        List<Genre> genreList = getAllGenres();
        for (Genre genre : genreList){
            Integer num=0;
            try{
                em.getTransaction().begin();
                num = em.createQuery("select b from Book b where b.genre_id = :value")
                        .setParameter("value", genre.getId()).getResultList().size();
                em.getTransaction().commit();
            } catch (Exception e){
                em.getTransaction().rollback();
            }
            resultMap.put(genre,num);
        }

        return resultMap;
    }
}
