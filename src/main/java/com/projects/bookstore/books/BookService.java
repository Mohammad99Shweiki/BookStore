package com.projects.bookstore.books;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    List<Book> getAllBooks();

    Set<Book> searchBooks(String search);

    Set<Book> getBooksPurchasedBy(String userId) throws Exception;

    Optional<Book> getBookById(String id);

    String addBook(BookDTO book);

    String updateBook(String id, Book book);

    void deleteBook(String id);

    void deleteAll();
}

