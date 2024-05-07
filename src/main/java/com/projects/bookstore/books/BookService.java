package com.projects.bookstore.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface BookService {
    Page<Book> getAllBooks(Pageable pageable);

    Set<Book> searchBooks(String search);

    Set<Book> getBooksPurchasedBy(String userId) throws Exception;

    Optional<Book> getBookById(String id);

    String addBook(Book book);

    String updateBook(String id, Book book);

    void deleteBook(String id);

    void deleteAll();
}

