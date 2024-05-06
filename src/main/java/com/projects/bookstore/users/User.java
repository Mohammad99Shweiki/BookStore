package com.projects.bookstore.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.bookstore.users.order.CartItem;
import com.projects.bookstore.users.order.Order;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Document(indexName = "users")
public class User {
    @Id
    private String userId;

    private String username;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    //todo hash
    private String password;

    private List<String> wishlist = new ArrayList<>();

    private Set<CartItem> cart = new HashSet<>();

    private Set<Order> orders = new HashSet<>();

    private UserRole role;

    List<String> favoriteGenres = new ArrayList<>();

    @Field(type = FieldType.Dense_Vector, store = true, dims = 384)
    @JsonIgnore
    private List<Float> embedding;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;
}
