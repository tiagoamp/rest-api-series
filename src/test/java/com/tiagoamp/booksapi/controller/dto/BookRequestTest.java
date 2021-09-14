package com.tiagoamp.booksapi.controller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookRequestTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    @DisplayName("When empty values Should return validation errors")
    public void testValidation_emptyValues() {
        BookRequest request = new BookRequest();
        Set<ConstraintViolation<BookRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty(), "Should have validation errors");
        List<String> requiredFieds = List.of("title", "language");
        errors.stream().forEach(e -> {
            String property = e.getPropertyPath().toString();
            assertTrue(requiredFieds.contains(property), "Should have required field validation error");
        });
    }

    @Test
    @DisplayName("When invalid values Should return validation errors")
    public void testValidation_invalidValues() {
        BookRequest request = new BookRequest("","",-1,"");
        Set<ConstraintViolation<BookRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty(), "Should have validation errors");
        assertTrue(errors.stream().anyMatch(e -> e.getPropertyPath().toString().equals("title")), "Should have title validation error");
        assertTrue(errors.stream().anyMatch(e -> e.getPropertyPath().toString().equals("language")), "Should have language validation error");
        assertTrue(errors.stream().anyMatch(e -> e.getPropertyPath().toString().equals("year")), "Should have title year error");
        assertTrue(errors.stream().anyMatch(e -> e.getPropertyPath().toString().equals("authors")), "Should have title authors error");
    }

    @Test
    @DisplayName("When valid values Should not return validation errors")
    public void testValidation_validValues() {
        BookRequest request = new BookRequest("Title","Latim",2022,"Author Name");
        Set<ConstraintViolation<BookRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty(), "Should not have validation errors");
    }

}