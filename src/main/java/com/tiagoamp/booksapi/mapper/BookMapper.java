package com.tiagoamp.booksapi.mapper;

import com.tiagoamp.booksapi.controller.dto.BookRequest;
import com.tiagoamp.booksapi.controller.dto.BookResponse;
import com.tiagoamp.booksapi.model.Book;
import com.tiagoamp.booksapi.repository.BookEntity;
import org.mapstruct.*;

@Mapper(componentModel="spring")
public interface BookMapper {

    Book toModel(BookRequest request);

    Book toModel(BookEntity entity);

    BookEntity toEntity(Book model);

    //@Mapping(source="model.id", target="id")
    BookResponse toResponse(Book model);

    BookRequest toRequest(Book model);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // updates only non-null values
    void updateEntityValues(BookEntity entitySource, @MappingTarget BookEntity entityUpdated);

}
