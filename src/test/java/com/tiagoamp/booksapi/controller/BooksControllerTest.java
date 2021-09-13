package com.tiagoamp.booksapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.mapper.BookMapperImpl;
import com.tiagoamp.booksapi.mapper.ReviewMapper;
import com.tiagoamp.booksapi.mapper.ReviewMapperImpl;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.service.BooksService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)  // disable security filters
@ComponentScan(basePackageClasses = {BookMapper.class, BookMapperImpl.class, ReviewMapper.class, ReviewMapperImpl.class})  // allows mapstruct mappers dep injection
class BooksControllerTest {

    @MockBean
    private BooksService booksService;

    @Autowired
    private BookMapperImpl bookMapper;
    @Autowired
    private ReviewMapperImpl reviewMapper;

    @Autowired
    private BooksController controller;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private List<Book> booksMock = List.of( new Book(1, "title 1", "lang 1", 2001, "author 1"),
            new Book(2, "title 2", "lang 2", 2002, "author 2"),
            new Book(3, "title 3", "lang 3", 2003, "author 3") );


    @Test
    @DisplayName("When Get All Books request and there are no results Should return empty list")
    public void whenGetAllRequest_emptyListResponse() throws Exception {
        Mockito.when(booksService.findAllBooks()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("When Get All Books request and there are registered books Should return list")
    public void whenGetAllRequest_resultListResponse() throws Exception {
        Mockito.when(booksService.findAllBooks()).thenReturn(booksMock);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", is(not(emptyArray()))))
                .andExpect(jsonPath("$", hasSize(booksMock.size())));
    }


    //TODO:

    // TESTS CASES:
    // 1) invalid input (validation)
    // 2) No results (no resource found exception)

    @Test
    @DisplayName("When Get Book by id request Should result book response")
    public void whenGetByIdRequest_resultResponse() throws Exception {
        Book bookMock = booksMock.get(0);
        Mockito.when(booksService.findBookById(Mockito.anyInt())).thenReturn(bookMock);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/book/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is( bookMock.getId() )))
                .andExpect(jsonPath("$.title").exists())
        ;
    }

}