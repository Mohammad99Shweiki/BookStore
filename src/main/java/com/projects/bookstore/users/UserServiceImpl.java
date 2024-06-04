package com.projects.bookstore.users;

import com.projects.bookstore.books.Book;
import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final RecommendationService recommendationService;

    @Override
    public User getById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        embedUserFavoriteGenres(user);
        return userRepository.save(user);
    }

    @Override
    //todo make another endpoint and method to order change password and another one for the role -- do it with authorization
    public String update(String userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (!user.getFavoriteGenres().isEmpty()) {
            existingUser.setFavoriteGenres(user.getFavoriteGenres());
            embedUserFavoriteGenres(existingUser);
        }
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getWishlist() != null) {
            existingUser.setWishlist(user.getWishlist());
        }
        if (user.getCart() != null) {
            existingUser.setCart(user.getCart());
        }
        if (user.getOrders() != null) {
            existingUser.setOrders(user.getOrders());
        }
        if (user.getWallet() != null) {
            existingUser.setWallet(user.getWallet());
        }
        return userRepository.save(existingUser).getUserId();
    }

    @Override
    public void delete(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new ObjectNotFoundException(userId);
        }
    }

    @Override
    public List<Book> recommendBooks(String userId) throws IOException {
        User user = getById(userId);
        List<Float> userEmbedding = user.getEmbedding();
        if (userEmbedding.isEmpty()) {
            throw new RuntimeException("make sure the user has favorite genres before ordering recommendation");
        }
        return recommendationService.knnQuery(userEmbedding);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.get();
        return user.get();
    }

    public void embedUserFavoriteGenres(User user) {
        if (!user.getFavoriteGenres().isEmpty()) {
            String userFavoriteGenres = String.join(";", user.getFavoriteGenres());
            List<Float> embedding = recommendationService.embedText(userFavoriteGenres);
            user.setEmbedding(embedding);
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
