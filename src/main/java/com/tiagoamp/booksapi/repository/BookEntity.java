package com.tiagoamp.booksapi.repository;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity  @Table(name = "BOOKS")
@Data
public class BookEntity {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String language;

    private Integer year;

    private String authors;

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="REVIEWS", joinColumns=@JoinColumn(name="BOOK_ID"))
    @Column(name="TEXT")
    private List<String> reviews = new ArrayList<>();

}
