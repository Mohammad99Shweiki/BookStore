package com.projects.bookstore.users.order;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Order {
    private String date;

    private Set<CartItem> items = new HashSet<>();

    private Double totalPrice;

    private OrderStatus status;

    private String address;

    private String phoneNo;

    public void setItems(Set<CartItem> items) {
        this.getItems().addAll(items);
    }
}
