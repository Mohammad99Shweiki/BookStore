package com.projects.bookstore.users.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Order {
    private String date;

    private Cart cart;

    private Double totalPrice;

    private OrderStatus status;

    private String address;

    private String phoneNo;
}
