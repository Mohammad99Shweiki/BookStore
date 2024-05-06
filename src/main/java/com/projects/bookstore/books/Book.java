package com.projects.bookstore.books;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Document(indexName = "books")
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @Id
    private String id;

    private String title;

    private List<String> authors = new ArrayList<>();

    private List<String> genres = new ArrayList<>();

    private Float price;

    private String description;

    private String publicationDate;

    private List<Rating> ratings = new ArrayList<>();

    private List<String> awards = new ArrayList<>();

    private String publisher;

    private String isbn;

    private String language;

    private Integer pages;

    private Integer available;

    private Integer sold;

    private String imageLink;

    private String fileLink;

    @Field(type = FieldType.Dense_Vector, store = true, dims = 384)
    private List<Float> embedding;
}