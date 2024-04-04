package com.projects.bookstore.users;

import com.projects.bookstore.books.Book;
import com.projects.bookstore.users.order.CartItem;
import com.projects.bookstore.users.order.Order;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Document(collection = "users")
public class User {
    @Id
    private String userId;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String nation;

    private String mobileNo;

    private String password;

    private List<Book> wishlist;

    private Set<CartItem> cart;

    private Set<Order> orders;

    private UserRole role;

    private String address;

    List<String> favoriteCategories;
}
