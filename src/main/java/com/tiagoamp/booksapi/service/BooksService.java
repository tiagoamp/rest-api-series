package com.tiagoamp.booksapi.service;

import com.tiagoamp.booksapi.exception.ResourceAlreadyExistsException;
import com.tiagoamp.booksapi.exception.ResourceNotFoundException;
import com.tiagoamp.booksapi.model.Book;
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

    public List<String> findReviewsOfBook(Integer bookId) {
        abortIfBookDoesNotExist(bookId);
        return booksRepo.findReviewsOfBook(bookId);
    }

    public String addReview(Integer bookId, String review) {
        abortIfBookDoesNotExist(bookId);
        return booksRepo.addReview(bookId, review);
    }


    private void abortIfBookDoesNotExist(Integer id) {
        booksRepo.find(id).orElseThrow(() -> new ResourceNotFoundException(Book.class.getSimpleName(), id.toString()));
    }

}
