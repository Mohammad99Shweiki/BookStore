package com.projects.bookstore.store;

import com.projects.bookstore.store.StoreService;
import com.projects.bookstore.users.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/addToCart/{userId}/{bookId}/{quantity}")
    public ResponseEntity<Void> addToCart(@PathVariable String userId, @PathVariable String bookId, @PathVariable int quantity) {
        storeService.addToCart(userId, bookId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/purchaseCart/{userId}")
    public ResponseEntity<Void> purchaseCart(@PathVariable String userId) {
        storeService.purchaseCart(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/purchaseHistory/{userId}")
    public ResponseEntity<Set<Order>> getPurchaseHistory(@PathVariable String userId) {
        Set<Order> purchaseHistory = storeService.getPurchaseHistory(userId);
        return ResponseEntity.ok(purchaseHistory);
    }
}
