package com.tiagoamp.booksapi.controller;

import com.tiagoamp.booksapi.controller.dto.BookRequest;
import com.tiagoamp.booksapi.controller.dto.BookResponse;
import com.tiagoamp.booksapi.controller.dto.ReviewRequest;
import com.tiagoamp.booksapi.controller.dto.ReviewResponse;
import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.mapper.ReviewMapper;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.model.Review;
import com.tiagoamp.booksapi.service.BooksService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/book")
@AllArgsConstructor
public class BooksController {

    private BooksService service;
    private BookMapper bookMapper;
    private ReviewMapper reviewMapper;


    // validation ???

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks() {
        List<Book> books = service.findAllBooks();
        List<BookResponse> booksResp = books.stream().map(bookMapper::toResponse).collect(toList());
        return ResponseEntity.ok(booksResp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Integer id) {
        Book book = service.findBookById(id);
        BookResponse bookResp = bookMapper.toResponse(book);
        return ResponseEntity.ok(bookResp);
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        Book book = bookMapper.toModel(request);
        book = service.createBook(book);
        BookResponse bookResp = bookMapper.toResponse(book);
        return ResponseEntity.created(URI.create(book.getId().toString())).body(bookResp);
    }

    @PutMapping("{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Integer id, @RequestBody BookRequest request) {
        Book book = bookMapper.toModel(request);
        book.setId(id);
        book = service.updateBook(book);
        BookResponse bookResp = bookMapper.toResponse(book);
        return ResponseEntity.ok(bookResp);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteBook(@PathVariable("id") Integer id) {
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("{bookId}/review")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable("bookId") Integer bookId) {
        List<Review> reviews = service.findReviewsOfBook(bookId);
        List<ReviewResponse> reviewsResp = reviews.stream().map(reviewMapper::toResponse).collect(toList());
        return ResponseEntity.ok(reviewsResp);
    }

    @GetMapping("{bookId}/review/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable("bookId") Integer bookId, @PathVariable("reviewId") Integer reviewId) {
        Review review = service.findReview(bookId, reviewId);
        ReviewResponse reviewResp = reviewMapper.toResponse(review);
        return ResponseEntity.ok(reviewResp);
    }

    @PostMapping("{bookId}/review")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable("bookId") Integer bookId, @RequestBody ReviewRequest request) {
        Review review = reviewMapper.toModel(request);
        review = service.createReview(bookId, review);
        ReviewResponse reviewResp = reviewMapper.toResponse(review);
        return ResponseEntity.created(URI.create(review.getId().toString())).body(reviewResp);
    }

}
