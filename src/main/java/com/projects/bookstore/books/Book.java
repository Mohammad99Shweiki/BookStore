package com.projects.bookstore.books;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
@Document(indexName = "books_2")
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @Id
    private String isbn;

    private String title;

    private List<String> authors = new ArrayList<>();

    private List<String> genres = new ArrayList<>();

    @NotNull
    private Float price = Float.parseFloat("0");

    private String description;

    private String publicationDate;

    private List<Rating> ratings = new ArrayList<>();

    private List<String> awards = new ArrayList<>();

    private String publisher;

    private String language;

    @NotNull
    private Integer pages;

    private Integer available = 0;

    private Integer sold = 0;

    private String imageLink;

    @Field(type = FieldType.Dense_Vector, dims = 384)
    @JsonIgnore
    private List<Float> embedding;

    private Boolean onSale = Boolean.FALSE;

    private Float salePrice;

}