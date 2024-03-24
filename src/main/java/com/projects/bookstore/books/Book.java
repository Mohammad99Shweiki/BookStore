package com.projects.bookstore.books;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book {

    @Id
    private String id;

    private String title;

    private List<String> authors;

    private String publisher;

    private List<String> genres;

    private String description;

    private String ISBN;

    private String publicationDate;

    private String price;

    private String abstractInfo;

    private String language;

    private Integer pages;
}