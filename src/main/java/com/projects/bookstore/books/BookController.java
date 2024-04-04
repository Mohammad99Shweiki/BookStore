package com.projects.bookstore.books;

import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
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
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        return bookOptional
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/purchased/{userId}")
    public ResponseEntity<Set<Book>> getBooksPurchasedBy(@PathVariable String userId) {
        try {
            Set<Book> books = bookService.getBooksPurchasedBy(userId);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Set<Book>> searchBooks(@RequestParam String search) {
        try {
            Set<Book> books = bookService.searchBooks(search);
            return ResponseEntity.ok(books);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity addBook(@RequestBody Book newBook) {
        Book createdBook = bookService.addBook(newBook);
        return createdBook != null ? ResponseEntity.status(HttpStatus.CREATED).body(createdBook.getId()) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        Optional<Book> bookOptional = Optional.ofNullable(bookService.updateBook(id, updatedBook));
        return bookOptional
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

