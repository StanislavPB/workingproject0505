package org.workingproject0505.repository;

import org.workingproject0505.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> updateUser(User user);

    Optional<User> deleteById(Integer id);

    List<User> findAll();

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    List<User> findByRole(String role);

    List<User> findByName(String username);


}
