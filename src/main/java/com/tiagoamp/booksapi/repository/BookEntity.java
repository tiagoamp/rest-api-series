package com.tiagoamp.booksapi.repository;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity  @Table(name = "BOOKS")
@Data
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String language;

    private Integer year;

    private String authors;

    @OneToMany(mappedBy = "book" , fetch = FetchType.LAZY)
    private List<ReviewEntity> reviews;

}
