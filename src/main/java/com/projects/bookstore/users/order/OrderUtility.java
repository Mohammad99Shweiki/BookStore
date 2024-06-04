package com.projects.bookstore.users.order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderUtility {
    public static OrdersReport makeReport(List<Order> orders) {
        return OrdersReport.builder()
                .orders(orders)
                .totalProfit(orders.stream()
                        .filter(order -> order.getStatus() == OrderStatus.OUT_FOR_DELIVERY || order.getStatus() == OrderStatus.DELIVERED)
                        .mapToDouble(Order::getTotalPrice)
                        .sum())
                .orderStatusMap(orders.stream()
                        .map(Order::getStatus)
                        .collect(Collectors.groupingBy(status -> status, Collectors.counting())))
                .build();
    }
}
