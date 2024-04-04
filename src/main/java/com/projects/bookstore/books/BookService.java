package com.projects.bookstore.books;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    List<Book> getAllBooks();

    Set<Book> getBooksPurchasedBy(String userId) throws Exception;

    Set<Book> searchBooks(String search);

    Optional<Book> getBookById(String id);

    Book addBook(Book book);

    Book updateBook(String id, Book book);

    void deleteBook(String id);
}

