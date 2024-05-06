package com.projects.bookstore.books;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByTitleContaining(String title);

//    todo fix
//    List<Book> findAllById(Iterable<String> ids);
}
