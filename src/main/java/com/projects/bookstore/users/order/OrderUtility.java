package com.projects.bookstore.users.order;

import java.util.List;

public class OrderUtility {
    public static OrdersReport makeReport(List<Order> orders) {
        return OrdersReport.builder()
                .orders(orders)
                .totalProfit(orders.stream()
                        .mapToDouble(Order::getTotalPrice)
                        .sum())
                .build();
    }
}
