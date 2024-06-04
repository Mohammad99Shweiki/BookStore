package com.projects.bookstore.users.order;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrdersReport {
    List<Order> orders;

    Map<OrderStatus, Long> orderStatusMap;

    Double totalProfit;
}
