package com.tiagoamp.booksapi.mapper;

import com.tiagoamp.booksapi.controller.dto.BookRequest;
import com.tiagoamp.booksapi.controller.dto.BookResponse;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.repository.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface BookMapper {

    Book toModel(BookRequest req);

    Book toModel(BookEntity entity);

    BookEntity toEntity(Book book);

    @Mapping(source="book.id", target="id")
    BookResponse toResponse(Book book);

}
