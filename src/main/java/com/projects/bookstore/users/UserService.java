package com.projects.bookstore.users;

import com.projects.bookstore.books.Book;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getUserById(String userId);

    List<User> getAllUsers();

    User createUser(User user);

    String updateUser(String userId, User user);

    void deleteUser(String userId);

    List<Book> RecommendBooks(String userId) throws IOException;
}
