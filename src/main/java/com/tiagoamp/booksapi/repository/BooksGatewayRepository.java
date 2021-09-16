package com.tiagoamp.booksapi.repository;

import com.tiagoamp.booksapi.controller.dto.ReviewRequest;
import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
@AllArgsConstructor
public class BooksGatewayRepository {

    private BookRepository bookRepo;
    private BookMapper bookMapper;


    public List<Book> findAll() {
        List<BookEntity> entities = bookRepo.findAll();
        return entities.stream().map(bookMapper::toModel).collect(toList());
    }

    public Optional<Book> find(Integer id) {
        Optional<BookEntity> entityOpt = bookRepo.findById(id);
        return mapToBookModelOptional(entityOpt);
    }

    public Book save(Book book) {
        BookEntity entity = bookMapper.toEntity(book);
        entity = bookRepo.save(entity);
        return bookMapper.toModel(entity);
    }

    public Optional<Book> findBookByTitle(String title) {
        Optional<BookEntity> entityOpt = bookRepo.findByTitle(title);
        return mapToBookModelOptional(entityOpt);
    }

    public Book update(Book book) {
        BookEntity entityForUpdate = bookRepo.getById(book.getId());
        BookEntity entityFromUpdatedModel = bookMapper.toEntity(book);
        bookMapper.updateEntityValues(entityFromUpdatedModel, entityForUpdate);
        bookRepo.save(entityForUpdate);
        return bookMapper.toModel(entityForUpdate);
    }

    public void delete(Integer id) {
        BookEntity entity = bookRepo.getById(id);
        bookRepo.delete(entity);
    }

    public List<String> findReviewsOfBook(Integer bookId) {
        BookEntity bookEntity = bookRepo.getById(bookId);
        List<String> reviews = bookEntity.getReviews();
        return reviews != null ? reviews : new ArrayList<>();
    }

    public String addReview(Integer bookId, String review) {
        BookEntity bookEntity = bookRepo.getById(bookId);
        bookEntity.getReviews().add(review);
        bookRepo.save(bookEntity);
        return review;
    }


    private Optional<Book> mapToBookModelOptional(Optional<BookEntity> entityOpt) {
        if (entityOpt.isEmpty())
            return Optional.empty();
        Book book = bookMapper.toModel(entityOpt.get());
        return Optional.of(book);
    }

}
