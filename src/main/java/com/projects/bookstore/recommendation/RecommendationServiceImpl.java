package com.projects.bookstore.recommendation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.KnnSearchResponse;
import co.elastic.clients.elasticsearch.core.knn_search.KnnSearchQuery;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.projects.bookstore.books.Book;
import com.projects.bookstore.common.EmbeddingRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ElasticsearchClient client;

    @Value("${model.url}")
    private String modelUrl;

    @Override
    public List embedText(String text) {
        RestTemplate restTemplate = new RestTemplate();
        EmbeddingRequestBody requestBody = EmbeddingRequestBody.builder()
                .content(text)
                .build();
        return restTemplate.postForObject(modelUrl, requestBody, List.class);
    }

    @Override
    public List<Book> knnQuery(List<Float> embedding) throws IOException {
        KnnSearchResponse<Book> response = client.knnSearch(s -> s.index("books")
                .knn(KnnSearchQuery.of(
                        query -> query
                                .numCandidates(50)
                                .k(10)
                                .field("embedding")
                                .queryVector(embedding)
                )), Book.class);

        return response.hits().hits().stream()
                .sorted((a, b) -> Objects.requireNonNull(b.score()).compareTo(Objects.requireNonNull(a.score())))
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }
}
