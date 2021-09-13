package com.tiagoamp.booksapi.mapper;

import com.tiagoamp.booksapi.controller.dto.ReviewRequest;
import com.tiagoamp.booksapi.controller.dto.ReviewResponse;
import com.tiagoamp.booksapi.model.Review;
import com.tiagoamp.booksapi.repository.ReviewEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ReviewMapper {

    Review toModel(ReviewRequest req);

    Review toModel(ReviewEntity entity);

    ReviewEntity toEntity(Review review);

    ReviewResponse toResponse(Review review);

}
