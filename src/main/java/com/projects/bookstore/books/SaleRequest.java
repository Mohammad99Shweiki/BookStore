package com.projects.bookstore.books;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleRequest {
    private String bookId;

    private Float salePrice;
}
