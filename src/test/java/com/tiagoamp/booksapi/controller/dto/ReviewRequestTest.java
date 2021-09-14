package com.tiagoamp.booksapi.controller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReviewRequestTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    @DisplayName("When empty values Should return validation errors")
    public void testValidation_emptyValues() {
        ReviewRequest request = new ReviewRequest();
        Set<ConstraintViolation<ReviewRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty(), "Should have validation errors");
        List<String> requiredFieds = List.of("text");
        errors.stream().forEach(e -> {
            String property = e.getPropertyPath().toString();
            assertTrue(requiredFieds.contains(property), "Should have required field validation error");
        });
    }

    @Test
    @DisplayName("When invalid values Should return validation errors")
    public void testValidation_invalidValues() {
        ReviewRequest request = new ReviewRequest("");
        Set<ConstraintViolation<ReviewRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty(), "Should have validation errors");
        assertTrue(errors.stream().anyMatch(e -> e.getPropertyPath().toString().equals("text")), "Should have text validation error");
    }

    @Test
    @DisplayName("When valid values Should not return validation errors")
    public void testValidation_validValues() {
        ReviewRequest request = new ReviewRequest("Review Text");
        Set<ConstraintViolation<ReviewRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty(), "Should not have validation errors");
    }

}