package com.tiagoamp.booksapi.repository;

import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.mapper.BookMapperImpl;
import com.tiagoamp.booksapi.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tiagoamp.booksapi.TestHelper.booksMock;
import static com.tiagoamp.booksapi.TestHelper.reviewsMock;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = {BookMapper.class, BookMapperImpl.class})  // allows mapstruct mappers dep injection
@DataJpaTest
class BooksGatewayRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private BookMapperImpl bookMapper;

    private BooksGatewayRepository repository;


    @BeforeEach
    void setup() {
        if (repository == null)
            repository = new BooksGatewayRepository(bookRepo, bookMapper);
    }


    @Test
    @DisplayName("When no books registered Should return empty list")
    void findAll_emptyList() {
        List<Book> result = repository.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When books registered Should return list")
    void findAll_resultList() {
        List<Book> added = insertBooksIntoDatabase();
        var result = repository.findAll();
        assertNotNull(result);
        assertEquals(added.size(), result.size());
    }


    @Test
    @DisplayName("When id not registered Should return empty result")
    void find_empty() {
        List<Book> added = insertBooksIntoDatabase();
        int notExistingId = -3;
        Optional<Book> result = repository.find(notExistingId);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When existing id Should return result")
    void find_result() {
        List<Book> added = insertBooksIntoDatabase();
        int id = added.get(0).getId();
        Optional<Book> result = repository.find(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }


    @Test
    @DisplayName("When saved book Should return result")
    void save() {
        // given
        Book book = booksMock.get(0);
        book.setId(null);
        // when
        Book result = repository.save(book);
        // then
        assertNotNull(result.getId());
    }


    @Test
    @DisplayName("When title not registered Should return empty result")
    void findBookByTitle_empty() {
        List<Book> added = insertBooksIntoDatabase();
        String notExistingTitle = "Not Existing Title";
        Optional<Book> result = repository.findBookByTitle(notExistingTitle);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When existing title Should return result")
    void findByTitle_result() {
        List<Book> added = insertBooksIntoDatabase();
        String title = added.get(0).getTitle();
        Optional<Book> result = repository.findBookByTitle(title);
        assertTrue(result.isPresent());
        assertEquals(title, result.get().getTitle());
    }


    @Test
    @DisplayName("When updated values Should return updated result")
    void update() {
        // given
        Book insertedBook = insertOneBookIntoDatabase();
        Book updatedBook = insertedBook;
        assertNotNull(updatedBook.getId(), "Mapped book should have id");
            // updating values
        updatedBook.setTitle("Updated Title");
        updatedBook.setLanguage("Updated Language");
        updatedBook.setYear(2000);
        updatedBook.setAuthors("Updated Authors");
        // when
        Book result = repository.update(updatedBook);
        // then
        assertEquals(insertedBook.getId(), result.getId());
        assertEquals(updatedBook.getTitle(), result.getTitle());
        assertEquals(updatedBook.getLanguage(), result.getLanguage());
        assertEquals(updatedBook.getYear(), result.getYear());
        assertEquals(updatedBook.getAuthors(), result.getAuthors());
    }


    @Test
    @DisplayName("When delete book Should result no errors")
    void delete() {
        Book insertedBook = insertOneBookIntoDatabase();
        Integer id = insertedBook.getId();
        assertNotNull(id, "Inserted book should have id");
        Optional<Book> bookRetrievedBeforeDelete = repository.find(id);
        assertTrue(bookRetrievedBeforeDelete.isPresent());
        // when
        repository.delete(id);
        // then
        Optional<Book> bookRetrievedAfterDelete = repository.find(id);
        assertTrue(bookRetrievedAfterDelete.isEmpty());
    }


    @Test
    @DisplayName("When book has no Reviews Should return empty list")
    void findReviewsOfBook_emptyList() {
        Book book = insertOneBookIntoDatabase();
        List<String> result = repository.findReviewsOfBook(book.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("When book has Reviews Should return list")
    void findReviewsOfBook_resultList() {
        Book book = insertBookWithReviewsIntoDatabase();
        List<String> result = repository.findReviewsOfBook(book.getId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }


    @Test
    @DisplayName("When first review Should return result")
    void addReview_first() {
        // given
        Book book = insertOneBookIntoDatabase();
        String newReview = "First Review";
        // when
        String result = repository.addReview(book.getId(), newReview);
        // then
        assertEquals(newReview, result);
        List<String> reviewsOfBook = repository.findReviewsOfBook(book.getId());
        assertFalse(reviewsOfBook.isEmpty());
        assertEquals(1, reviewsOfBook.size());
        assertEquals(newReview, reviewsOfBook.get(0));
    }

    @Test
    @DisplayName("When new review Should return result")
    void addReview_newReview() {
        // given
        Book book = insertBookWithReviewsIntoDatabase();
        final int qtyReviewsBeforeInsert = repository.findReviewsOfBook(book.getId()).size();
        String newReview = "New Test Review";
        // when
        String result = repository.addReview(book.getId(), newReview);
        // then
        assertEquals(newReview, result);
        List<String> reviewsOfBook = repository.findReviewsOfBook(book.getId());
        assertFalse(reviewsOfBook.isEmpty());
        assertEquals(qtyReviewsBeforeInsert + 1, reviewsOfBook.size(), "Should have the new inserted review plus the previous registered one");
        assertTrue(reviewsOfBook.contains(newReview));
    }


    private List<Book> insertBooksIntoDatabase() {
        List<Book> books = new ArrayList<>(booksMock);
        List<BookEntity> entities = books.stream().map(bookMapper::toEntity).collect(toList());
        List<BookEntity> persisted = entities.stream().map(e -> {
            e.setId(null);
            e = entityManager.persist(e);
            entityManager.flush();
            return e;
        }).collect(toList());
        return persisted.stream().map(bookMapper::toModel).collect(toList());
    }

    private Book insertOneBookIntoDatabase() {
        List<Book> books = new ArrayList<>(booksMock);
        BookEntity entity = bookMapper.toEntity(books.get(0));
        entity.setId(null);
        entity = entityManager.persist(entity);
        entityManager.flush();
        return bookMapper.toModel(entity);
    }

    private Book insertBookWithReviewsIntoDatabase() {
        List<Book> books = new ArrayList<>(booksMock);
        BookEntity entity = bookMapper.toEntity(books.get(0));
        entity.setReviews(new ArrayList<>(reviewsMock));
        entity.setId(null);
        entity = entityManager.persist(entity);
        entityManager.flush();
        return bookMapper.toModel(entity);
    }

}