package com.tiagoamp.booksapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class BookRequest {

    private String title;
    private String language;
    private Integer year;
    private String authors;

}
