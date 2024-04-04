package com.projects.bookstore.books;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private String userId;

    private Integer rating;
}