package com.haulmont.testtask.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTHOR")
@NamedQuery(name = "AUTHOR.getAll", query = "SELECT a from Author a")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Override
    public String toString(){
        return firstname+" "+lastname;
    }
}
