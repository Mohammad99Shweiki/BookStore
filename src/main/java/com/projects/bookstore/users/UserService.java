package com.projects.bookstore.users;

import com.projects.bookstore.books.BookDTO;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getUserById(String userId);

    List<User> getAllUsers();

    User createUser(UserDTO user);

    User updateUser(String userId, User user);

    void deleteUser(String userId);

    List<BookDTO> RecommendBooks(String userId) throws IOException;
}
