package com.projects.bookstore.books;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    private String title;

    private List<String> authors;

    private String genre;

    private double price;

    private String description;

    private List<String> formats;

    private String publicationDate;

    private List<Rating> ratings;

    private String publisher;

    private String ISBN;

    private String language;

    private Integer pages;

    private Integer available;

    private Integer sold;

    private String imageLink;

    private String fileLink;
}