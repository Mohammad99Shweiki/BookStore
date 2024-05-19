package com.projects.bookstore.store;

import com.projects.bookstore.users.order.Order;
import com.projects.bookstore.users.order.OrderRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/{userId}/{bookId}/{quantity}")
    public ResponseEntity<Void> addToCart(@PathVariable String userId, @PathVariable String bookId, @PathVariable int quantity) {
        storeService.addToCart(userId, bookId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{bookId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable String userId, @PathVariable String bookId) {
        storeService.removeFromCart(userId, bookId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        storeService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/purchaseCart/{userId}")
    public ResponseEntity<Void> purchaseCart(@PathVariable String userId, @RequestBody @NotNull OrderRequest orderRequest) {
        storeService.purchaseCart(userId, orderRequest);
        return ResponseEntity.ok().build();
    }

    //todo make it a report
    @GetMapping("/purchaseHistory/{userId}")
    public ResponseEntity<Set<Order>> getPurchaseHistory(@PathVariable String userId) {
        Set<Order> purchaseHistory = storeService.getPurchaseHistory(userId);
        return ResponseEntity.ok(purchaseHistory);
    }
}
