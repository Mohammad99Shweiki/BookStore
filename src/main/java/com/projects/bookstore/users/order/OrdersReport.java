package com.projects.bookstore.users.order;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrdersReport {
    List<Order> orders;

    Double totalProfit;
}
