package com.tiagoamp.booksapi.mapper;

import com.tiagoamp.booksapi.controller.dto.ReviewRequest;
import com.tiagoamp.booksapi.controller.dto.ReviewResponse;
import com.tiagoamp.booksapi.model.Review;
import com.tiagoamp.booksapi.repository.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface ReviewMapper {

    Review toModel(ReviewRequest request);

    Review toModel(ReviewEntity entity);

    ReviewEntity toEntity(Review model);

    ReviewResponse toResponse(Review model);

    @Mapping(source="model.text", target="text")
    ReviewRequest toRequest(Review model);

}
