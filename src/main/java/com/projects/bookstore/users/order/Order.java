package com.projects.bookstore.users.order;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Order {
    private String orderId;

    private LocalDate date;

    private Set<CartItem> items;

    private Double totalPrice;

    private String shippingAddress;

    private String status;

    private String address;

    private String phoneNo;
}
