package com.projects.bookstore.store;

import com.projects.bookstore.books.Book;
import com.projects.bookstore.books.BookService;
import com.projects.bookstore.common.exceptions.NotEnoughWalletException;
import com.projects.bookstore.users.User;
import com.projects.bookstore.users.UserService;
import com.projects.bookstore.users.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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


        CartItem cartItem;
        if (user.getCart().getItems().containsKey(bookId)) {
            cartItem = user.getCart().getItems().get(bookId);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            Float itemPrice = book.getOnSale() ? book.getSalePrice() : book.getPrice();
            cartItem.setTotalPrice(cartItem.getTotalPrice() + (quantity * (itemPrice)));
            user.getCart().setTotalPrice(user.getCart().getTotalPrice() + (quantity * cartItem.getPrice()));
        } else {
            cartItem = CartItem.builder()
                    .bookTitle(book.getTitle())
                    .bookAuthors(book.getAuthors())
                    .quantity(quantity)
                    .price(book.getPrice())
                    .totalPrice((quantity * book.getPrice()))
                    .build();
            user.getCart().getItems().put(bookId, cartItem);
            user.getCart().setTotalPrice(user.getCart().getTotalPrice() + (quantity * book.getPrice()));
        }
        userService.save(user);
    }

    @Override
    public void removeFromCart(String userId, String bookId) {
        User user = userService.getById(userId);

        user.getCart().setTotalPrice(user.getCart().getTotalPrice() - user.getCart().getItems().get(bookId).getTotalPrice());
        user.getCart().getItems().remove(bookId);

        userService.save(user);
    }

    @Override
    public void clearCart(String userId) {
        User user = userService.getById(userId);

        user.getCart().getItems().clear();
        user.getCart().setTotalPrice(0.0);

        userService.save(user);
    }

    @Override
    @Transactional
    public void purchaseCart(String userId, OrderRequest orderRequest) {
        User user = userService.getById(userId);

        if (user.getWallet() < user.getCart().getTotalPrice()) {
            throw new NotEnoughWalletException();
        }

        Order order = new Order();
        order.setCart(user.getCart());

        Set<String> genres = new HashSet<>();
        user.getCart().getItems().forEach((key, value) -> {

            Book book = bookService.getById(key);
            int quantitySold = value.getQuantity();
            book.setSold(book.getSold() + quantitySold);
            genres.addAll(book.getGenres());
            bookService.save(book);
        });

        order.setDate(LocalDate.now().toString());
        order.setAddress(orderRequest.getAddress());
        order.setPhoneNo(orderRequest.getPhoneNo());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalPrice(user.getCart().getTotalPrice());

        user.getFavoriteGenres().addAll(genres);
        userService.embedUserFavoriteGenres(user);

        if (user.getOrders() == null) {
            user.setOrders(new OrdersEntity());
        }

        user.getOrders().getOrders().add(order);
        user.getOrders().setTotal(user.getOrders().getTotal() + order.getTotalPrice());

        user.setCart(new Cart());

        user.setWallet(user.getWallet() - order.getTotalPrice());
        userService.save(user);
    }

    @Override
    public OrdersEntity getPurchaseHistory(String userId) {
        User user = userService.getById(userId);
        if (user.getOrders() == null) {
            user.setOrders(OrdersEntity.builder()
                    .total(0.0)
                    .build());
        }
        return user.getOrders();
    }

    @Override
    public Cart getUserCart(String userId) {
        return userService.getById(userId).getCart();
    }

    @Override
    public OrdersReport getOrdersReport() {
        List<Order> orders = userService.getAll()
                .stream()
                .map(user -> user.getOrders().getOrders())
                .flatMap(Set::stream)
                .toList();
        return OrderUtility.makeReport(orders);
    }
}
