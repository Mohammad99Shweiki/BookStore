package com.projects.bookstore.users;

import co.elastic.clients.elasticsearch.core.KnnSearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.projects.bookstore.books.Book;
import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<User> getAll(Pageable pageable) {
        Iterable<User> iterable = userRepository.findAll(pageable);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        embedUserFavoriteGenres(user);
        return userRepository.save(user);
    }

    @Override
    //todo make another endpoint and method to order change password -- do it with authorization
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
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
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

    //todo put names in configuration instead of hard coding names
    @Override
    public List<Book> recommendBooks(String userId) throws IOException {
        User user = getById(userId);
        //todo throw exception when empty
        List<Float> userEmbedding = user.getEmbedding();
        KnnSearchResponse<Book> queryResult = recommendationService.knnQuery(userEmbedding);
        return queryResult.hits().hits().stream()
                .sorted((a, b) -> Objects.requireNonNull(b.score()).compareTo(Objects.requireNonNull(a.score())))
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
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

}
