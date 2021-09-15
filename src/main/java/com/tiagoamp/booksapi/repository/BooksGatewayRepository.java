package com.tiagoamp.booksapi.repository;

import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.mapper.ReviewMapper;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.model.Review;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
@AllArgsConstructor
public class BooksGatewayRepository {

    private BookRepository bookRepo;
    private ReviewRepository reviewRepo;
    private BookMapper bookMapper;
    private ReviewMapper reviewMapper;


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

    public List<Review> findReviewsOfBook(Integer bookId) {
        BookEntity bookEntity = bookRepo.getById(bookId);
        List<ReviewEntity> reviewEntities = bookEntity.getReviews();
        return reviewEntities.stream().map(reviewMapper::toModel).collect(toList());
    }

    public Optional<Review> findReview(Integer reviewId) {
        Optional<ReviewEntity> entityOpt = reviewRepo.findById(reviewId);
        return mapToReviewModelOptional(entityOpt);
    }

    public Review save(Integer bookId, Review review) {
        BookEntity bookEntity = bookRepo.getById(bookId);
        ReviewEntity reviewEntity = reviewMapper.toEntity(review);
        reviewEntity.setBook(bookEntity);
        reviewEntity = reviewRepo.save(reviewEntity);
        return reviewMapper.toModel(reviewEntity);
    }

    public void deleteReview(Integer reviewId) {
        ReviewEntity entity = reviewRepo.getById(reviewId);
        reviewRepo.delete(entity);
    }


    private Optional<Book> mapToBookModelOptional(Optional<BookEntity> entityOpt) {
        if (entityOpt.isEmpty())
            return Optional.empty();
        Book book = bookMapper.toModel(entityOpt.get());
        return Optional.of(book);
    }

    private Optional<Review> mapToReviewModelOptional(Optional<ReviewEntity> entityOpt) {
        if (entityOpt.isEmpty())
            return Optional.empty();
        Review review = reviewMapper.toModel(entityOpt.get());
        return Optional.of(review);
    }

}
