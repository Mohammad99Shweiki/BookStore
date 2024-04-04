package com.projects.bookstore.users;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsernameContaining(String username);

    List<User> findAllByRole(UserRole role);

    Optional<User> findByUsername(String username);

    Optional<User> findByUserId(String userId);
}
