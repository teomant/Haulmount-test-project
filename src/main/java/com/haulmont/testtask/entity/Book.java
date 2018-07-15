package com.haulmont.testtask.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOK")
@NamedQuery(name = "BOOK.getAll", query = "SELECT b from Book b")
public class Book {

    public enum Publisher {
        Moscow,
        Piter,
        OReilly
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "GENRE_ID")
    private Genre genre;

    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    @Column(name = "PUBLICATION_CITY")
    private String publicationCity;

    @Column(name = "PUBLISHER")
    @Enumerated(EnumType.STRING)
    private Publisher publisher;

    @Override
    public String toString(){
        return name+" by "+author.toString();
    }

}
