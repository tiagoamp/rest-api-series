package com.tiagoamp.booksapi.controller;

import com.tiagoamp.booksapi.controller.dto.BookRequest;
import com.tiagoamp.booksapi.controller.dto.BookResponse;
import com.tiagoamp.booksapi.controller.dto.ReviewRequest;
import com.tiagoamp.booksapi.controller.dto.ReviewResponse;
import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.service.BooksService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/book")
@AllArgsConstructor
public class BooksController {

    private BooksService service;
    private BookMapper bookMapper;


    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks() {
        List<Book> books = service.findAllBooks();
        List<BookResponse> booksResp = books.stream()
                .map(bookMapper::toResponse)
                .map(b -> b.add( linkTo(methodOn(this.getClass()).getBook(b.getId())).withSelfRel() ))
                .collect(toList());
        return ResponseEntity.ok(booksResp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Integer id) {
        Book book = service.findBookById(id);
        BookResponse bookResp = bookMapper.toResponse(book)
                .add( linkTo(methodOn(this.getClass()).getReviews(id)).withRel("reviews") )
                .add( linkTo(methodOn(this.getClass()).getBooks()).withRel("books") );
        return ResponseEntity.ok(bookResp);
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
        Book book = bookMapper.toModel(request);
        book = service.createBook(book);
        BookResponse bookResp = bookMapper.toResponse(book);
        return ResponseEntity.created(URI.create(book.getId().toString())).body(bookResp);
    }

    @PutMapping("{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Integer id, @RequestBody @Valid BookRequest request) {
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
        List<String> reviews = service.findReviewsOfBook(bookId);
        List<ReviewResponse> reviewsResp = reviews.stream()
                .map(ReviewResponse::new)
                .map(r -> r.add( linkTo(methodOn(this.getClass()).getBook(bookId)).withRel("book") ))
                .collect(toList());
        return ResponseEntity.ok(reviewsResp);
    }

    @PostMapping("{bookId}/review")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable("bookId") Integer bookId, @RequestBody @Valid ReviewRequest request) {
        String review = request.getReview();
        review = service.addReview(bookId, review);
        ReviewResponse reviewResp = new ReviewResponse(review);
        return ResponseEntity.created(URI.create("/")).body(reviewResp);
    }

}
