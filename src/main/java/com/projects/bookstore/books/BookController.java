package com.projects.bookstore.books;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(Pageable pageable) {
        Page<Book> books = bookService.getAll(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable String id) {
        Book book = bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(@RequestParam String search, Pageable pageable) {
        Page<Book> books = bookService.searchBooks(search, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/purchased/{userId}")
    public ResponseEntity<Set<Book>> getBooksPurchasedBy(@PathVariable String userId) throws Exception {
        Set<Book> books = bookService.getBooksPurchasedBy(userId);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/")
    public ResponseEntity<String> add(@RequestBody Book newBook) {
        String bookId = bookService.save(newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody Book updatedBook) {
        String bookId = bookService.update(id, updatedBook);
        return ResponseEntity.of(Optional.ofNullable(bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        bookService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
