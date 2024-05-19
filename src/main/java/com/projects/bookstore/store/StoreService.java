package com.projects.bookstore.store;

import com.projects.bookstore.users.order.Order;
import com.projects.bookstore.users.order.OrderRequest;

import java.util.Set;

public interface StoreService {
    void addToCart(String userId, String bookId, Integer quantity);

    void removeFromCart(String userId, String bookId);

    void clearCart(String userId);

    void purchaseCart(String userId, OrderRequest orderRequest);

    Set<Order> getPurchaseHistory(String userId);
}
