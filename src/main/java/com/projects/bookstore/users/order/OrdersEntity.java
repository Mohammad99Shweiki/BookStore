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
public class OrdersEntity {
    private Set<Order> orders = new HashSet<>();

    private Double total = 0.0;
}
