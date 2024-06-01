package com.projects.bookstore.recommendation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.KnnSearchResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.knn_search.KnnSearchQuery;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.projects.bookstore.books.Book;
import com.projects.bookstore.common.EmbeddingRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ElasticsearchClient client;

    @Value("${model.url}")
    private String modelUrl;

    @Override
    public List<Float> embedText(String text) {
        RestTemplate restTemplate = new RestTemplate();
        EmbeddingRequestBody requestBody = EmbeddingRequestBody.builder()
                .content(text)
                .build();
        List<Double> list = restTemplate.postForObject(modelUrl, requestBody, List.class);

        return list.stream()
                .map(Double::floatValue)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> knnQuery(List<Float> embedding) throws IOException {
        KnnSearchResponse<Book> response = knnSearch(embedding);

        return response.hits().hits().stream()
                .sorted((a, b) -> Objects.requireNonNull(b.score()).compareTo(Objects.requireNonNull(a.score())))
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }

    public KnnSearchResponse<Book> knnSearch(List<Float> embedding) throws IOException {
        KnnSearchResponse<Book> response = client.knnSearch(s -> s.index("books")
                .knn(KnnSearchQuery.of(
                        query -> query
                                .numCandidates(50)
                                .k(10)
                                .field("embedding")
                                .queryVector(embedding)
                )), Book.class);
        return response;
    }

    public List<Book> search(String text, String fieldName, List<Float> embedding) throws IOException {
        SearchResponse<Book> textSearchResponse = client.search(SearchRequest.of(
                s -> s.index("books")
                        .query(q -> q
                                .match(m -> m
                                        .field(fieldName)
                                        .query(text)
                                )
                        )
        ), Book.class);


        KnnSearchResponse<Book> knnSearchResponse = knnSearch(embedding);

        Map<String, Hit<Book>> combinedResults = new LinkedHashMap<>();

        extractBooks(textSearchResponse.hits().hits(), combinedResults);

        extractBooks(knnSearchResponse.hits().hits(), combinedResults);
        return combinedResults.values().stream()
                .sorted((a, b) -> Objects.requireNonNull(b.score()).compareTo(Objects.requireNonNull(a.score())))
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    private void extractBooks(List<Hit<Book>> hits, Map<String, Hit<Book>> combinedResults) {
        for (Hit<Book> hit : hits) {
            if (hit.source() != null) {
                combinedResults.put(hit.id(), hit);
            }
        }
    }
}
