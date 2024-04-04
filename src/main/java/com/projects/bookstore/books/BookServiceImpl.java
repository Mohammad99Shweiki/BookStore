package com.projects.bookstore.books;

import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserRepository;
import com.projects.bookstore.users.order.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Set<Book> searchBooks(String search) {
        return new HashSet<>(bookRepository.findByTitleContaining(search));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Book> getBooksPurchasedBy(String userId) throws ObjectNotFoundException {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<String> bookIds = user.getOrders().stream()
                    .flatMap(order -> order.getItems().stream().map(CartItem::getBookId))
                    .collect(Collectors.toSet());
            return new HashSet<>(bookRepository.findAll(bookIds));
        } else {
            throw new ObjectNotFoundException(userId);
        }
    }

    @Override
    @Transactional
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional
    public String addBook(Book book) {
        return bookRepository.save(book).getId();
    }

    @Override
    @Transactional
    public String updateBook(String id, Book book) {
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
            if (book.getGenre() != null) {
                existingBook.setGenre(book.getGenre());
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
            existingBook.setPrice(book.getPrice());
            if (book.getDescription() != null) {
                existingBook.setDescription(book.getDescription());
            }
            if (book.getLanguage() != null) {
                existingBook.setLanguage(book.getLanguage());
            }
            if (book.getPages() != null) {
                existingBook.setPages(book.getPages());
            }
            return bookRepository.save(existingBook).getId();
        }
        return null;
    }


    @Override
    @Transactional
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}