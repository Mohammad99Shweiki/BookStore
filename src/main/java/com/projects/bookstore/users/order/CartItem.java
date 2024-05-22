package com.projects.bookstore.users.order;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CartItem {
    private String bookTitle;

    private List<String> bookAuthors = new ArrayList<>();

    private Integer quantity;

    private Float price;

    private Float totalPrice;
}
