package com.tiagoamp.booksapi.repository;

import com.tiagoamp.booksapi.mapper.BookMapper;
import com.tiagoamp.booksapi.model.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Repository
@AllArgsConstructor
public class BooksRepository {

    private BookRepository bookRepo;
    private BookMapper bookMapper;


    public List<Book> findAll() {
        List<BookEntity> entities = bookRepo.findAll();
        return entities.stream().map(bookMapper::toModel).collect(toList());
    }

}
