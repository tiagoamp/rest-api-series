package com.tiagoamp.booksapi;

import com.tiagoamp.booksapi.model.Book;

import java.util.List;

public class TestHelper {

    public static final List<Book> booksMock = List.of( new Book(1, "title 1", "lang 1", 2001, "author 1"),
            new Book(2, "title 2", "lang 2", 2002, "author 2"),
            new Book(3, "title 3", "lang 3", 2003, "author 3") );

    public static final List<String> reviewsMock = List.of( "Review Text 01", "Review Text 02", "Review Text 03" );

}
