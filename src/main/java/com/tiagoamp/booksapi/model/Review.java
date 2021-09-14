package com.tiagoamp.booksapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  @AllArgsConstructor
public class Review {

    private Integer id;
    private String text;

}
