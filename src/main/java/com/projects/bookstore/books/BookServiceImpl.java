package com.projects.bookstore.books;

import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.recommendation.RecommendationService;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserRepository;
import com.projects.bookstore.users.order.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final RecommendationService recommendationService;

    @Override
    @Transactional
    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public List<Book> searchBooks(String search) throws IOException {
        List<Float> embedding = recommendationService.embedText(search);
        return recommendationService.knnQuery(embedding);
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
    public List<Book> getSimilar(String id) throws IOException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id));
        List<Float> embedding = book.getEmbedding();
        return recommendationService.knnQuery(embedding)
                .stream().filter(
                        bookObject -> !Objects.equals(bookObject.getTitle(), book.getTitle())
                )
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Book getById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
    }

    @Override
    public Page<Book> getOnSale(Pageable pageable) {
        return bookRepository.findByOnSaleIsTrue(pageable);
    }

    @Override
    @Transactional
    public String save(Book book) {
        updateEmbedding(book);
        return bookRepository.save(book).getIsbn();
    }

    @Override
    @Transactional
    public String update(String id, Book book) {
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
            return bookRepository.save(existingBook).getIsbn();
        }
        return null;
    }

    @Override
    public String saleOffer(SaleRequest saleRequest) {
        Book book = getById(saleRequest.getBookId());
        if (saleRequest.getSalePrice() >= book.getPrice()) {
            throw new RuntimeException("the sale price should be lower than actual price");
        }
        book.setOnSale(true);
        book.setSalePrice(saleRequest.getSalePrice());
        return bookRepository.save(book).getIsbn();
    }


    @Override
    @Transactional
    public void delete(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    private void updateEmbedding(Book book) {
        List<Float> embedding = recommendationService.embedText(book.getDescription());
        book.setEmbedding(embedding);
    }
}