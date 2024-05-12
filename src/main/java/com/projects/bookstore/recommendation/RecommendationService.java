package com.projects.bookstore.recommendation;

import com.projects.bookstore.books.Book;

import java.io.IOException;
import java.util.List;

public interface RecommendationService {

    List<Float> embedText(String text);

    List<Book> knnQuery(List<Float> embedding) throws IOException;
}
