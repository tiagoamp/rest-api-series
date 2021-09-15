package com.tiagoamp.booksapi.service;

import com.tiagoamp.booksapi.exception.ResourceAlreadyExistsException;
import com.tiagoamp.booksapi.exception.ResourceNotFoundException;
import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.mapper.ReviewMapper;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.model.Review;
import com.tiagoamp.booksapi.repository.BooksGatewayRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

    @Mock
    private BooksGatewayRepository repo;

    @Spy  // injects this specific instance to target class
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
    @Spy
    private ReviewMapper reviewMapper = Mappers.getMapper(ReviewMapper.class);

    @InjectMocks
    private BooksService service;

    private List<Book> booksMock = List.of( new Book(1, "title 1", "lang 1", 2001, "author 1"),
            new Book(2, "title 2", "lang 2", 2002, "author 2"),
            new Book(3, "title 3", "lang 3", 2003, "author 3") );

    private Book bookMock = booksMock.get(0);

    private List<Review> reviewsMock = List.of( new Review(10, "Review Text 01"),
            new Review(20, "Review Text 02"), new Review(30, "Review Text 03") );


    @Test
    @DisplayName("When no books registered should return empty list")
    void findAllBooks_emptyList() {
        Mockito.when(repo.findAll()).thenReturn(new ArrayList<>());
        List<Book> result = service.findAllBooks();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When there are books registered should return list")
    void findAllBooks_resultList() {
        Mockito.when(repo.findAll()).thenReturn(booksMock);
        List<Book> result = service.findAllBooks();
        assertFalse(result.isEmpty());
        assertEquals(booksMock.size(), result.size());
    }

    @Test
    @DisplayName("When id does not exist should throw exception")
    void findBookById_exception() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findBookById(1));
    }

    @Test
    @DisplayName("When id exists should return result")
    void findBookById_result() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.of(bookMock));
        Book result = service.findBookById(bookMock.getId());
        assertEquals(bookMock.getId(), result.getId());
    }

    @Test
    @DisplayName("When book already registered should throw exception")
    void createBook_exception() {
        Mockito.when(repo.findBookByTitle(Mockito.anyString())).thenReturn(Optional.of(bookMock));
        assertThrows(ResourceAlreadyExistsException.class, () -> service.createBook(bookMock));
    }

    @Test
    @DisplayName("When book not registered should insert book")
    void createBook_result() {
        Mockito.when(repo.findBookByTitle(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(repo.save(Mockito.any(Book.class))).thenReturn(bookMock);
        Book result = service.createBook(bookMock);
        assertEquals(bookMock.getId(), result.getId());
    }

    @Test
    @DisplayName("When book does not exist should throw exception")
    void updateBook_exception() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findBookById(1));
    }

    @Test
    @DisplayName("When book exists should update values")
    void updateBook_result() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.of(bookMock));
        Mockito.when(repo.update(Mockito.any(Book.class))).thenReturn(bookMock);
        Book result = service.updateBook(bookMock);
        assertEquals(bookMock.getId(), result.getId());
    }

    @Test
    @DisplayName("When book does not exist should throw exception")
    void deleteBook_exception() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findBookById(1));
    }

    @Test
    @DisplayName("When book exists should delete book")
    void deleteBook_result() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.of(bookMock));
        assertDoesNotThrow(() -> service.updateBook(bookMock));
    }

    @Test
    @DisplayName("When book does not exist should throw exception")
    void findReviewsOfBook_exception() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findBookById(1));
    }

    @Test
    @DisplayName("When there are no reviews should return empty list")
    void findReviewsOfBook_emptyList() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.of(bookMock));
        Mockito.when(repo.findReviewsOfBook(Mockito.anyInt())).thenReturn(new ArrayList<>());
        List<Review> result = service.findReviewsOfBook(bookMock.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When there are registered reviews should return list")
    void findReviewsOfBook_result() {
        Mockito.when(repo.find(Mockito.anyInt())).thenReturn(Optional.of(bookMock));
        Mockito.when(repo.findReviewsOfBook(Mockito.anyInt())).thenReturn(reviewsMock);
        List<Review> result = service.findReviewsOfBook(bookMock.getId());
        assertFalse(result.isEmpty());
        assertEquals(booksMock.size(), result.size());
    }


    @Test
    void findReview() {
    }

    @Test
    void createReview() {
    }

    @Test
    void deleteReview() {
    }
}