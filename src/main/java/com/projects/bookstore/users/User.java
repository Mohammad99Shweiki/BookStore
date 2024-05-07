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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Document(indexName = "users")
public class User implements UserDetails {
    @Id
    private String userId;

    private String username;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    @JsonIgnore
    private String password;

    private Set<String> wishlist;

    private Set<CartItem> cart;

    private Set<Order> orders;

    @JsonIgnore
    private UserRole role;

    Set<String> favoriteGenres = new HashSet<>();

    @Field(type = FieldType.Dense_Vector, store = true, dims = 384)
    @JsonIgnore
    private List<Float> embedding;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        simpleGrantedAuthorityList.add(new SimpleGrantedAuthority("CUSTOMER"));
        return simpleGrantedAuthorityList;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
