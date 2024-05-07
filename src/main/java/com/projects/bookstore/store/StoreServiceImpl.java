package com.projects.bookstore.store;

import com.projects.bookstore.books.Book;
import com.projects.bookstore.books.BookService;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserService;
import com.projects.bookstore.users.order.CartItem;
import com.projects.bookstore.users.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    public void purchaseCart(String userId) {
        User user = userService.getById(userId);

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setItems(user.getCart());

        Set<String> genres = new HashSet<>();
        order.getItems()
                .forEach(item -> {
                    Book book = bookService.getById(item.getBookId());
                    int quantitySold = item.getQuantity();
                    book.setSold(book.getSold() + quantitySold);
                    genres.addAll(book.getGenres());
                    bookService.save(book);
                });

        user.getFavoriteGenres().addAll(genres);
        userService.embedUserFavoriteGenres(user);

        user.getOrders().add(order);
        user.getCart().clear();
        userService.save(user);
    }

    @Override
    public Set<Order> getPurchaseHistory(String userId) {
        User user = userService.getById(userId);
        return user.getOrders();
    }
}
