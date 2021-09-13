package com.tiagoamp.booksapi.repository;

import lombok.Data;

import javax.persistence.*;

@Entity  @Table(name = "REVIEWS")
@Data
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    @ManyToOne
    @JoinColumn(name="bookId", nullable=false)
    private BookEntity book;

}
