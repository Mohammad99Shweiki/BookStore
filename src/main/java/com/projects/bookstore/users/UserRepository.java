package com.projects.bookstore.users;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {
    List<User> findByUsernameContaining(String username);

    List<User> findAllByRole(UserRole role);

    Boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUserId(String userId);
}
