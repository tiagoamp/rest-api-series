package com.tiagoamp.booksapi.service;

import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.model.Review;
import com.tiagoamp.booksapi.repository.BooksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BooksService {

    private BooksRepository booksRepo;


    public List<Book> findAllBooks() {
        return booksRepo.findAll();
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

    public void deleteReview(Integer bookId, Integer reviewId) {
        return;
    }

}
