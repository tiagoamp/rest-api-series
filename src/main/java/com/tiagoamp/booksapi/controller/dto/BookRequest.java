package com.tiagoamp.booksapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor @AllArgsConstructor
public class BookRequest {

    @NotEmpty(message = "{required.field}")
    @Size(min = 1, max = 200, message = "{invalid.field}")
    private String title;

    @NotEmpty(message = "{required.field}")
    @Size(min = 1, max = 50, message = "{invalid.field}")
    private String language;

    @PositiveOrZero(message = "{invalid.field}")
    private Integer year;

    @Size(min = 1, max = 200, message = "{invalid.field}")
    private String authors;

}
