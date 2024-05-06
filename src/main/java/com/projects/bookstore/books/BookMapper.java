package com.projects.bookstore.books;

import co.elastic.clients.util.DateTime;

public class BookMapper {

    public static BookDTO toDto(Book book) {
        return BookDTO.builder()
                .title(book.getTitle())
                .authors(book.getAuthors())
                .genres(book.getGenres())
                .price(book.getPrice())
                .description(book.getDescription())
                .publicationDate(book.getPublicationDate())
                .awards(book.getAwards())
                .publisher(book.getPublisher())
                .isbn(book.getIsbn())
                .language(book.getLanguage())
                .pages(book.getPages())
                .imageLink(book.getImageLink())
                .fileLink(book.getFileLink())
                .build();
    }

    public static Book fromDto(BookDTO dto) {
        return Book.builder()
                .title(dto.getTitle())
                .authors(dto.getAuthors())
                .genres(dto.getGenres())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .publicationDate(dto.getPublicationDate())
                .awards(dto.getAwards())
                .publisher(dto.getPublisher())
                .isbn(dto.getIsbn())
                .language(dto.getLanguage())
                .pages(dto.getPages())
                .imageLink(dto.getImageLink())
                .fileLink(dto.getFileLink())
                .build();
    }
}
