package com.projects.bookstore.users;

import com.projects.bookstore.books.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable String userId) {
        User user = userService.getById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<List<Book>> recommendBooks(@PathVariable String userId) throws IOException {
        List<Book> books = userService.recommendBooks(userId);
        return ResponseEntity.ok(books);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(Pageable pageable) {
        List<User> users = userService.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> update(@PathVariable String userId, @RequestBody User user) {
        String updatedUser = userService.update(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
