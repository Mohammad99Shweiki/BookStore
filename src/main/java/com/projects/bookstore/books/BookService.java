package com.projects.bookstore.books;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(String id);

    Book addBook(Book book);

    Book updateBook(String id, Book book);

    void deleteBook(String id);
}

