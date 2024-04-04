package com.projects.bookstore.store;

import com.projects.bookstore.books.Book;
import com.projects.bookstore.books.BookRepository;
import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserRepository;
import com.projects.bookstore.users.order.CartItem;
import com.projects.bookstore.users.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void addToCart(String userId, String bookId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException(bookId));

        CartItem existingCartItem = user.getCart().stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            user.getCart().add(new CartItem(bookId, quantity));
        }

        userRepository.save(user);
    }

    @Override
    public void removeFromCart(String userId, String bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));

        user.getCart().removeIf(item -> item.getBookId().equals(bookId));

        userRepository.save(user);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));

        user.getCart().clear();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void purchaseCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setItems(user.getCart());

        user.getOrders().add(order);
        user.getCart().clear();
        userRepository.save(user);
    }

    @Override
    public Set<Order> getPurchaseHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));
        return user.getOrders();
    }
}
