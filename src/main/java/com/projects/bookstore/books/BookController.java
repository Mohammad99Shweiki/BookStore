package com.projects.bookstore.books;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@CrossOrigin("*")
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public ResponseEntity<Page<Book>> getAll(Pageable pageable) {
        Page<Book> books = bookService.getAll(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable String id) {
        Book book = bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/similar/{id}")
    public ResponseEntity<List<Book>> getSimilar(@PathVariable String id) throws IOException {
        List<Book> similarBooks = bookService.getSimilar(id);
        return ResponseEntity.ok(similarBooks);
    }

    @GetMapping("/search/")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) throws IOException {
        List<Book> books = bookService.searchBooks(query);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/onSale")
    public ResponseEntity<Page<Book>> getOnSale(Pageable pageable) {
        Page<Book> books = bookService.getOnSale(pageable);
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

    @PutMapping("/sale")
    public ResponseEntity<String> saleOffer(@RequestBody @NotNull SaleRequest saleRequest) {
        String bookId = bookService.saleOffer(saleRequest);
        return ResponseEntity.ok(bookId);
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
