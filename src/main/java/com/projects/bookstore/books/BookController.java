package com.projects.bookstore.books;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        return bookOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }   

    @GetMapping("/search")
    public ResponseEntity<Set<Book>> searchBooks(@RequestParam String search) {
        Set<Book> books = bookService.searchBooks(search);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/")
    public ResponseEntity<String> addBook(@RequestBody Book newBook) {
        String bookId = bookService.addBook(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        String bookId = bookService.updateBook(id, updatedBook);
        return ResponseEntity.of(Optional.ofNullable(bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
