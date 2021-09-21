package com.tiagoamp.booksapi.controller;

import com.tiagoamp.booksapi.controller.dto.RootEntryPointResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RootEntryPointController {

    @GetMapping
    public ResponseEntity<RootEntryPointResponse> getRoot() {
        RootEntryPointResponse resp = new RootEntryPointResponse()
                .add( linkTo(methodOn(BooksController.class).getBooks()).withRel("books") );
        return ResponseEntity.ok(resp);
    }

}
