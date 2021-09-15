package com.tiagoamp.booksapi.controller.dto;

import lombok.Data;

@Data
public class BookResponse {

    private Integer id;
    private String title;
    private String language;
    private Integer year;
    private String authors;

}
