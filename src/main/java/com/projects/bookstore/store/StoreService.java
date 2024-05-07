package com.projects.bookstore.store;

import com.projects.bookstore.users.order.Order;

import java.util.Set;
//todo add unused methods to the controller
public interface StoreService {
    void addToCart(String userId, String bookId, Integer quantity);

    void removeFromCart(String userId, String bookId);

    void clearCart(String userId);

    void purchaseCart(String userId);

    Set<Order> getPurchaseHistory(String userId);
}
