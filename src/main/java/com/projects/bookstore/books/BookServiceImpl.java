package com.projects.bookstore.books;

import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.recommendation.NLPService;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserRepository;
import com.projects.bookstore.users.order.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final NLPService NLPService;

    @Override
    @Transactional
    public List<Book> getAllBooks() {
        Iterable<Book> iterable = bookRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
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
            return new HashSet<>((Collection<? extends Book>) bookRepository.findAllById(bookIds));
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
    //todo specify type of exception
    public String addBook(BookDTO bookDTO) {
        Book book = BookMapper.fromDto(bookDTO);
        updateEmbedding(book);
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
                existingBook.getAuthors().addAll(book.getAuthors());
            }
            if (book.getPublisher() != null) {
                existingBook.setPublisher(book.getPublisher());
            }
            if (book.getGenres() != null && !book.getGenres().isEmpty()) {
                existingBook.getGenres().addAll(book.getGenres());
            }
            if (book.getDescription() != null) {
                existingBook.setDescription(book.getDescription());
                updateEmbedding(book);
            }
            if (book.getIsbn() != null) {
                existingBook.setIsbn(book.getIsbn());
            }
            if (book.getPublicationDate() != null) {
                existingBook.setPublicationDate(book.getPublicationDate());
            }
            if (book.getPrice() != 0.0) {
                existingBook.setPrice(book.getPrice());
            }
            if (book.getLanguage() != null) {
                existingBook.setLanguage(book.getLanguage());
            }
            if (book.getPages() != null) {
                existingBook.setPages(book.getPages());
            }
            if (book.getFileLink() != null) {
                existingBook.setFileLink(book.getFileLink());
            }
            if (book.getImageLink() != null) {
                existingBook.setImageLink(book.getImageLink());
            }
            if (book.getAvailable() != null) {
                existingBook.setAvailable(book.getAvailable());
            }
            if (book.getSold() != null) {
                existingBook.setSold(book.getSold());
            }
            if (book.getRatings() != null && !book.getRatings().isEmpty()) {
                existingBook.getRatings().addAll(book.getRatings());
            }
            if (book.getAwards() != null && !book.getAwards().isEmpty()) {
                existingBook.getAwards().addAll(book.getAwards());
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

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    private void updateEmbedding(Book book) {
        List<Float> embedding = NLPService.embedText(book.getDescription());
        book.setEmbedding(embedding);
    }
}