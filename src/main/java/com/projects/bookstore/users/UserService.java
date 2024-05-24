package com.projects.bookstore.users;

import com.projects.bookstore.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getById(String userId);

    Page<User> getAll(Pageable pageable);

    User save(User user);

    String update(String userId, User user);

    void delete(String userId);

    List<Book> recommendBooks(String userId) throws IOException;

    void embedUserFavoriteGenres(User user);

    Boolean existsByEmail(String email);
}
