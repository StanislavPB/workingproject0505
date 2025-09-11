package org.workingproject0505.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workingproject0505.entity.Role;
import org.workingproject0505.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByUserName(String username);


}
