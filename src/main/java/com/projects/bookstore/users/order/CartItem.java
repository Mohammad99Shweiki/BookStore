package com.projects.bookstore.users.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CartItem {
    private String bookId;

    private Integer quantity;
}
