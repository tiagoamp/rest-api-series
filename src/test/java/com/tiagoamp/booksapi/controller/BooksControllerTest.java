package com.tiagoamp.booksapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoamp.booksapi.controller.dto.BookRequest;
import com.tiagoamp.booksapi.controller.dto.ReviewRequest;
import com.tiagoamp.booksapi.exception.ResourceAlreadyExistsException;
import com.tiagoamp.booksapi.exception.ResourceNotFoundException;
import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.mapper.BookMapperImpl;
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

import static com.tiagoamp.booksapi.TestHelper.booksMock;
import static com.tiagoamp.booksapi.TestHelper.reviewsMock;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)  // disable security filters
@ComponentScan(basePackageClasses = {BookMapper.class, BookMapperImpl.class})  // allows mapstruct mappers dep injection
class BooksControllerTest {

    @MockBean
    private BooksService booksService;

    @Autowired
    private BookMapperImpl bookMapper;

    @Autowired
    private BooksController controller;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();


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


    @Test
    @DisplayName("When Get Book by non-existing id Should return error")
    public void whenGetByNonExistingIdRequest_resultError() throws Exception {
        Integer reqId = 1;
        Mockito.when(booksService.findBookById(Mockito.anyInt())).thenThrow(new ResourceNotFoundException(Book.class.getSimpleName(), reqId.toString()));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/book/{id}", reqId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").doesNotExist())
                .andExpect(jsonPath("$.title", is( ResourceNotFoundException.class.getSimpleName() )))
                .andExpect(jsonPath("$.message", containsString( Book.class.getSimpleName())) )
                .andExpect(jsonPath("$.message", containsString( reqId.toString())) );
    }

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


    @Test
    @DisplayName("When Post with invalid values request Should result validation error")
    public void whenPostInvalidValuesRequest_resultError() throws Exception {
        BookRequest invalidReq = new BookRequest();
        String json = jsonMapper.writeValueAsString(invalidReq);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/book")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("ValidationException")))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.details.title").exists())
                .andExpect(jsonPath("$.details.language").exists());
    }

    @Test
    @DisplayName("When Post request of a existing entity Should result error")
    public void whenPostExistingRequest_resultError() throws Exception {
        Book bookMock = booksMock.get(0);
        BookRequest req = bookMapper.toRequest(bookMock);
        String json = jsonMapper.writeValueAsString(req);
        Mockito.when(booksService.createBook(Mockito.any(Book.class))).thenThrow(new ResourceAlreadyExistsException(Book.class.getSimpleName(), bookMock.getId().toString()));
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/book")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").doesNotExist())
                .andExpect(jsonPath("$.title", is( ResourceAlreadyExistsException.class.getSimpleName() )))
                .andExpect(jsonPath("$.message", containsString( Book.class.getSimpleName())) )
                .andExpect(jsonPath("$.message", containsString( bookMock.getId().toString())) );
    }

    @Test
    @DisplayName("When Post with valid request Should result book response")
    public void whenPostValidRequest_resultResponse() throws Exception {
        Book bookMock = booksMock.get(0);
        BookRequest req = bookMapper.toRequest(bookMock);
        String json = jsonMapper.writeValueAsString(req);
        Mockito.when(booksService.createBook(Mockito.any(Book.class))).thenReturn(bookMock);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/book")
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is( bookMock.getId() )))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.language").exists())
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.authors").exists());
    }


    @Test
    @DisplayName("When Put with invalid values request Should result validation error")
    public void whenPutInvalidValuesRequest_resultError() throws Exception {
        BookRequest invalidReq = new BookRequest();
        String json = jsonMapper.writeValueAsString(invalidReq);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/book/{id}",1)
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("ValidationException")))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.details.title").exists())
                .andExpect(jsonPath("$.details.language").exists());
    }

    @Test
    @DisplayName("When Put request of a non-existing id Should result error")
    public void whenPutNonExistingIdRequest_resultError() throws Exception {
        Book bookMock = booksMock.get(0);
        BookRequest req = bookMapper.toRequest(bookMock);
        String json = jsonMapper.writeValueAsString(req);
        Mockito.when(booksService.updateBook(Mockito.any(Book.class))).thenThrow(new ResourceNotFoundException(Book.class.getSimpleName(), bookMock.getId().toString()));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/book/{id}", bookMock.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").doesNotExist())
                .andExpect(jsonPath("$.title", is( ResourceNotFoundException.class.getSimpleName() )))
                .andExpect(jsonPath("$.message", containsString( Book.class.getSimpleName())) )
                .andExpect(jsonPath("$.message", containsString( bookMock.getId().toString())) );
    }

    @Test
    @DisplayName("When Put with valid request Should result book response")
    public void whenPutValidRequest_resultResponse() throws Exception {
        Book bookMock = booksMock.get(0);
        BookRequest req = bookMapper.toRequest(bookMock);
        String json = jsonMapper.writeValueAsString(req);
        Mockito.when(booksService.updateBook(Mockito.any(Book.class))).thenReturn(bookMock);
        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/book/{id}", bookMock.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is( bookMock.getId() )))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.language").exists())
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.authors").exists());
    }


    @Test
    @DisplayName("When Delete request of a non-existing id Should result error")
    public void whenDeleteNonExistingIdRequest_resultError() throws Exception {
        Integer reqId = 1;
        Mockito.doThrow(new ResourceNotFoundException(Book.class.getSimpleName(), reqId.toString())).when(booksService).deleteBook(Mockito.anyInt());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/book/{id}", reqId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").doesNotExist())
                .andExpect(jsonPath("$.title", is( ResourceNotFoundException.class.getSimpleName() )))
                .andExpect(jsonPath("$.message", containsString( Book.class.getSimpleName())) )
                .andExpect(jsonPath("$.message", containsString( reqId.toString())) );
    }

    @Test
    @DisplayName("When Delete with valid request Should result response")
    public void whenDeleteRequest_resultResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/book/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("When Get All Reviews of a book request and there are no results Should return empty list")
    public void whenGetAllReviewsRequest_emptyListResponse() throws Exception {
        Mockito.when(booksService.findReviewsOfBook(Mockito.anyInt())).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/book/{bookId}/review", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("When Get All Reviews of a book request and there are registered books Should return list")
    public void whenGetAllReviewsRequest_resultListResponse() throws Exception {
        Mockito.when(booksService.findReviewsOfBook(Mockito.anyInt())).thenReturn(reviewsMock);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/book/{bookId}/review", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", is(not(emptyArray()))))
                .andExpect(jsonPath("$", hasSize(reviewsMock.size())));
    }


    @Test
    @DisplayName("When Post with invalid Review values request Should result validation error")
    public void whenPostReviewInvalidValuesRequest_resultError() throws Exception {
        ReviewRequest invalidReq = new ReviewRequest();
        String json = jsonMapper.writeValueAsString(invalidReq);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/book/{bookId}/review", 1)
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("ValidationException")))
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.details.review").exists());
    }

    @Test
    @DisplayName("When Post with valid request Should result book response")
    public void whenPostReviewValidRequest_resultResponse() throws Exception {
        String reviewMock = reviewsMock.get(0);
        String json = jsonMapper.writeValueAsString( new ReviewRequest(reviewMock) );
        Mockito.when(booksService.addReview(Mockito.anyInt(), Mockito.anyString())).thenReturn(reviewMock);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/book/{bookId}/review", 1)
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.review").exists());
    }

}