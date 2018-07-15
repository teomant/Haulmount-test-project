package com.haulmont.testtask.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GENRE")
@NamedQuery(name = "GENRE.getAll", query = "SELECT g from Genre g")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Override
    public String toString(){
        return name;
    }

}
