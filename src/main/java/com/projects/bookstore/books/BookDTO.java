package com.projects.bookstore.books;

import co.elastic.clients.util.DateTime;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class BookDTO {
    @NotNull
    private String title;

    @NotNull
    private List<String> authors;

    @NotNull
    private List<String> genres;

    @NotNull
    private float price;

    @NotNull
    private String description;

//    @DateTimeFormat
    private String publicationDate;

    private List<String> awards;

    private String publisher;

    @ISBN
    private String isbn;

    private String language;

    private Integer pages;

    private String imageLink;

    private String fileLink;
}
