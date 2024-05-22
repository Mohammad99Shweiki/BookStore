package com.projects.bookstore.users.order;

import lombok.*;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Cart {
    private HashMap<String, CartItem> items = new HashMap<>();

    private Double totalPrice = 0.0;
}
