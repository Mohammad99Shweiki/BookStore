package com.projects.bookstore.store;

import com.projects.bookstore.books.Book;
import com.projects.bookstore.books.BookService;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserService;
import com.projects.bookstore.users.order.CartItem;
import com.projects.bookstore.users.order.Order;
import com.projects.bookstore.users.order.OrderRequest;
import com.projects.bookstore.users.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Override
    public void addToCart(String userId, String bookId, Integer quantity) {
        User user = userService.getById(userId);

        Book book = bookService.getById(bookId);

        if (user.getCart() == null)
            user.setCart(new HashSet<>());

        CartItem existingCartItem = user.getCart().stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            user.getCart().add(new CartItem(bookId, quantity));
        }

        userService.save(user);
    }

    @Override
    public void removeFromCart(String userId, String bookId) {
        User user = userService.getById(userId);

        user.getCart().removeIf(item -> item.getBookId().equals(bookId));

        userService.save(user);
    }

    @Override
    public void clearCart(String userId) {
        User user = userService.getById(userId);

        user.getCart().clear();

        userService.save(user);
    }

    @Override
    @Transactional
    public void purchaseCart(String userId, OrderRequest orderRequest) {
        User user = userService.getById(userId);

        Order order = new Order();
        order.setItems(user.getCart());

        Set<String> genres = new HashSet<>();
        AtomicReference<Double> total = new AtomicReference<>(0d);
        user.getCart()
                .forEach(item -> {
                    Book book = bookService.getById(item.getBookId());
                    int quantitySold = item.getQuantity();
                    book.setSold(book.getSold() + quantitySold);
                    genres.addAll(book.getGenres());
                    total.updateAndGet(v -> (v + quantitySold * book.getPrice()));
                    bookService.save(book);
                });

        order.setDate(LocalDate.now().toString());
        order.setAddress(orderRequest.getAddress());
        order.setPhoneNo(orderRequest.getPhoneNo());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalPrice(total.get());

        user.getFavoriteGenres().addAll(genres);
        userService.embedUserFavoriteGenres(user);

        user.getOrders().add(order);
        user.getCart().clear();
        userService.save(user);
    }

    @Override
    public Set<Order> getPurchaseHistory(String userId) {
        User user = userService.getById(userId);
        if (user.getOrders() == null)
            user.setOrders(new HashSet<>());
        return user.getOrders();
    }
}
