package com.tiagoamp.booksapi.controller.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class BookResponse extends RepresentationModel<BookResponse> {

    private Integer id;
    private String title;
    private String language;
    private Integer year;
    private String authors;

}
