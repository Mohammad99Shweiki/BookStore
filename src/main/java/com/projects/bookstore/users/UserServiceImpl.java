package com.projects.bookstore.users;

import co.elastic.clients.elasticsearch.core.KnnSearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.projects.bookstore.books.Book;
import com.projects.bookstore.books.BookDTO;
import com.projects.bookstore.books.BookMapper;
import com.projects.bookstore.common.exceptions.ObjectNotFoundException;
import com.projects.bookstore.recommendation.NLPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final NLPService nlpService;

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));
    }

    @Override
    public List<User> getAllUsers() {
        Iterable<User> iterable = userRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = UserMapper.fromDto(userDTO);
        embedUserFavoriteGenres(user);
        return userRepository.save(user);
    }


    @Override
    public User updateUser(String userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(userId));
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        //todo add embedding genres and fix the method for all fields
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new ObjectNotFoundException(userId);
        }
    }

    //todo put names in configuration instead of hard coding names
    @Override
    public List<BookDTO> RecommendBooks(String userId) throws IOException {
        User user = getUserById(userId);
        List<Float> userEmbedding = user.getEmbedding();
        KnnSearchResponse<Book> queryResult = nlpService.knnQuery(userEmbedding);
        return queryResult.hits().hits().stream()
                .sorted((a, b) -> Objects.requireNonNull(b.score()).compareTo(Objects.requireNonNull(a.score())))
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(BookMapper::toDto)
                .toList();
    }

    private void embedUserFavoriteGenres(User user) {
        if (!user.getFavoriteGenres().isEmpty()) {
            String userFavoriteGenres = String.join(";", user.getFavoriteGenres());
            List<Float> embedding = nlpService.embedText(userFavoriteGenres);
            user.setEmbedding(embedding);
        }
    }

}
