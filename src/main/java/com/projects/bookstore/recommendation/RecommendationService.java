package com.projects.bookstore.recommendation;

import co.elastic.clients.elasticsearch.core.KnnSearchResponse;
import com.projects.bookstore.books.Book;

import java.io.IOException;
import java.util.List;

public interface RecommendationService {

    List<Float> embedText(String text);

    KnnSearchResponse<Book> knnQuery(List<Float> embedding) throws IOException;
}
