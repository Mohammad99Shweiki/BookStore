package com.projects.bookstore.recommendation;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.KnnSearchResponse;
import co.elastic.clients.elasticsearch.core.knn_search.KnnSearchQuery;
import com.projects.bookstore.books.Book;
import com.projects.bookstore.common.EmbeddingRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ElasticsearchClient client;

    //todo add url for config
    @Override
    public List embedText(String text) {
        String url = "http://localhost:9090/embed_text";
        RestTemplate restTemplate = new RestTemplate();
        EmbeddingRequestBody requestBody = EmbeddingRequestBody.builder()
                .content(text)
                .build();
        return restTemplate.postForObject(url, requestBody, List.class);
    }

    @Override
    public KnnSearchResponse<Book> knnQuery(List<Float> embedding) throws IOException {
        return client.knnSearch(s -> s.index("books")
                .knn(KnnSearchQuery.of(
                        query -> query
                                .numCandidates(50)
                                .k(5)
                                .field("embedding")
                                .queryVector(embedding)
                )), Book.class);
    }
}
