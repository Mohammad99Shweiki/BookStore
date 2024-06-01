package com.projects.bookstore.books;

import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final RecommendationService recommendationService;

    @Override
    @Transactional
    public Page<Book> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Book> searchBooks(String search, Pageable pageable) throws IOException {
        List<Float> embedding = recommendationService.embedText(search);
        List<Book> books = recommendationService.search(search, "authors", embedding);
        return paginate(pageable, books);
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
        List<Float> embedding = recommendationService.embedText(book.getTitle() + ' ' + book.getDescription());
        book.setEmbedding(embedding);
    }

    private PageImpl<Book> paginate(Pageable pageable, List<Book> books) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), books.size());
        List<Book> pageContent = books.subList(start, end);

        return new PageImpl<>(pageContent, pageable, books.size());
    }
}