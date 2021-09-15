package com.tiagoamp.booksapi.service;

import com.tiagoamp.booksapi.exception.ResourceAlreadyExistsException;
import com.tiagoamp.booksapi.exception.ResourceNotFoundException;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.model.Review;
import com.tiagoamp.booksapi.repository.BooksGatewayRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BooksService {

    private BooksGatewayRepository booksRepo;


    public List<Book> findAllBooks() {
        return booksRepo.findAll();
    }

    public Book findBookById(Integer id) {
        return booksRepo.find(id)
                .orElseThrow(() -> new ResourceNotFoundException(Book.class.getSimpleName(), id.toString()));
    }

    public Book createBook(Book book) {
        Optional<Book> registeredBook = booksRepo.findBookByTitle(book.getTitle());
        if (registeredBook.isPresent())
            throw new ResourceAlreadyExistsException(Book.class.getSimpleName(), registeredBook.get().getId().toString());
        return booksRepo.save(book);
    }

    public Book updateBook(Book book) {
        abortIfBookDoesNotExist(book.getId());
        Book updated = booksRepo.update(book);
        return updated;
    }

    public void deleteBook(Integer id) {
        abortIfBookDoesNotExist(id);
        booksRepo.delete(id);
    }

    public List<Review> findReviewsOfBook(Integer bookId) {
        abortIfBookDoesNotExist(bookId);
        return booksRepo.findReviewsOfBook(bookId);
    }

    public Review findReview(Integer bookId, Integer reviewId) {
        return findReviewsOfBook(bookId).stream().filter(r -> r.getId().intValue() == reviewId.intValue()).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(Review.class.getSimpleName(), reviewId.toString());
    }

    public Review createReview(Integer bookId, Review review) {
        abortIfBookDoesNotExist(bookId);
        List<Review> reviews = findReviewsOfBook(bookId);
        Optional<Review> registeredReview = reviews.stream().filter(r -> r.getText().equals(review.getText())).findFirst();
        if (registeredReview.isPresent())
            throw new ResourceAlreadyExistsException(Review.class.getSimpleName(), registeredReview.get().getId().toString());
        return booksRepo.save(bookId, review);
    }

    public void deleteReview(Integer bookId, Integer reviewId) {
        abortIfReviewIdDoesNotBelongToBook(bookId, reviewId);
        booksRepo.deleteReview(reviewId);
    }


    private void abortIfBookDoesNotExist(Integer id) {
        booksRepo.find(id)
                .orElseThrow(() -> new ResourceNotFoundException(Book.class.getSimpleName(), id.toString()));
    }

    private void abortIfReviewIdDoesNotBelongToBook(Integer bookId, Integer reviewId) {
        abortIfBookDoesNotExist(bookId);
        boolean containsReviewId = booksRepo.findReviewsOfBook(bookId).stream().anyMatch(r -> r.getId().intValue() == reviewId.intValue());
        if (!containsReviewId)
            throw new ResourceNotFoundException(Review.class.getSimpleName(), reviewId.toString());
    }

}
