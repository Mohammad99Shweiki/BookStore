package com.projects.bookstore.store;

import com.projects.bookstore.users.order.Cart;
import com.projects.bookstore.users.order.OrderRequest;
import com.projects.bookstore.users.order.OrdersEntity;
import com.projects.bookstore.users.order.OrdersReport;

public interface StoreService {
    void addToCart(String userId, String bookId, Integer quantity);

    void removeFromCart(String userId, String bookId);

    void clearCart(String userId);

    void purchaseCart(String userId, OrderRequest orderRequest);

    OrdersEntity getPurchaseHistory(String userId);

    Cart getUserCart(String userId);

    OrdersReport getOrdersReport();
}
