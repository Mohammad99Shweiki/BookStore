package com.projects.bookstore.users;

import java.util.HashSet;

public class UserMapper {
    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setFavoriteGenres(user.getFavoriteGenres());
        dto.setWishlist(user.getWishlist());
        dto.setEmbedding(user.getEmbedding());
        return dto;
    }

    public static User fromDto(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getFirstName() + " " + dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setWishlist(dto.getWishlist());
        user.setFavoriteGenres(dto.getFavoriteGenres());
        return user;
    }
}
