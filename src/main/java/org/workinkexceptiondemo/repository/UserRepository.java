package org.workinkexceptiondemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workinkexceptiondemo.entity.Role;
import org.workinkexceptiondemo.entity.User;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByUserName(String username);


}
