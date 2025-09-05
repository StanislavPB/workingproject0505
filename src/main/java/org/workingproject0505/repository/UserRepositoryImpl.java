package org.workingproject0505.repository;

import org.springframework.stereotype.Repository;
import org.workingproject0505.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users;

    private Integer userIdCounter;

    public UserRepositoryImpl() {
        this.users = new ArrayList<>();
        this.userIdCounter = 0;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userIdCounter);
            users.add(user);
        } else {
            // если id есть - то обновляем запись в коллекции
            // удаляем старый объект и кладем новый
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
        }

        return user;
    }

      @Override
    public Optional<User> deleteById(Integer id) {
        Optional<User> userForDeleteOptional = findById(id);
        if (userForDeleteOptional.isEmpty()) {
            return Optional.empty();
        } else {
            users.remove(userForDeleteOptional.get());
            return userForDeleteOptional;
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findByRole(String role) {
        return users.stream()
                .filter(user -> user.getRole().name().equals(role))
                .toList();
    }

    @Override
    public List<User> findByName(String username) {
        return users.stream()
                .filter(user -> user.getUserName().equals(username))
                .toList();
    }
}
