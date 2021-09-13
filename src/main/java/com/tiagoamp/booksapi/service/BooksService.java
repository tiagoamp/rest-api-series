package com.tiagoamp.booksapi.service;

import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    public List<Book> findAllBooks() {
        return null;
    }

    public Book findBookById(Integer id) {
        return null;
    }

    public Book createBook(Book book) {
        return null;
    }

    public Book updateBook(Book book) {
        return null;
    }

    public void deleteBook(Integer id) {
        return;
    }

    public List<Review> findReviewsOfBook(Integer bookId) {
        return null;
    }

    public Review findReview(Integer bookId, Integer reviewId) {
        return null;
    }

    public Review createReview(Integer bookId, Review review) {
        return null;
    }

}
