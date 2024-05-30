package com.projects.bookstore.books;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    Page<Book> findByTitleContaining(String title, Pageable pageable);

    Page<Book> findByOnSaleIsTrue(Pageable pageable);
}
