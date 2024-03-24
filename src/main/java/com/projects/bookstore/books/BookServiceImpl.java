package com.projects.bookstore.books;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(String id, Book book) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            if (book.getTitle() != null) {
                existingBook.setTitle(book.getTitle());
            }
            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                existingBook.setAuthors(book.getAuthors());
            }
            if (book.getPublisher() != null) {
                existingBook.setPublisher(book.getPublisher());
            }
            if (book.getGenres() != null && !book.getGenres().isEmpty()) {
                existingBook.setGenres(book.getGenres());
            }
            if (book.getDescription() != null) {
                existingBook.setDescription(book.getDescription());
            }
            if (book.getISBN() != null) {
                existingBook.setISBN(book.getISBN());
            }
            if (book.getPublicationDate() != null) {
                existingBook.setPublicationDate(book.getPublicationDate());
            }
            if (book.getPrice() != null) {
                existingBook.setPrice(book.getPrice());
            }
            if (book.getAbstractInfo() != null) {
                existingBook.setAbstractInfo(book.getAbstractInfo());
            }
            if (book.getLanguage() != null) {
                existingBook.setLanguage(book.getLanguage());
            }
            if (book.getPages() != null) {
                existingBook.setPages(book.getPages());
            }
            return bookRepository.save(existingBook);
        }
        return null;
    }


    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}