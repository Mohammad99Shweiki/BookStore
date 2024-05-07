package com.projects.bookstore.books;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BookService {
    Page<Book> getAll(Pageable pageable);

    Page<Book> searchBooks(String search, Pageable pageable);

    Set<Book> getBooksPurchasedBy(String userId) throws Exception;

    Book getById(String id);

    String save(Book book);

    String update(String id, Book book);

    void delete(String id);

    void deleteAll();
}

