package org.workinkexceptiondemo.service;



import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workinkexceptiondemo.dto.GeneralResponse;
import org.workinkexceptiondemo.dto.UserRequestDto;
import org.workinkexceptiondemo.dto.UserResponseDto;
import org.workinkexceptiondemo.dto.UserUpdateRequestDto;
import org.workinkexceptiondemo.entity.Role;
import org.workinkexceptiondemo.entity.User;
import org.workinkexceptiondemo.repository.RoleRepository;
import org.workinkexceptiondemo.repository.UserRepository;
import org.workinkexceptiondemo.service.exception.AlreadyExistException;
import org.workinkexceptiondemo.service.exception.NotFoundException;
import org.workinkexceptiondemo.service.exception.ValidationException;
import org.workinkexceptiondemo.service.util.UserConverter;
import org.workinkexceptiondemo.service.validation.UserValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserConverter converter;
    private final UserValidation userValidation;
    private final RoleRepository roleRepository;

    @Value("${my.service.role}")
    private String roleByDefault = "";

    public UserResponseDto createUser(UserRequestDto request) {



        // проверяем запрос на соответствие полученных данных нашим критериям
        //makeRequestValidation(request);

        // проверка на то что такого пользователя еще нет
        makeAlreadyExistEmail(request.getEmail());

        // создание нового пользователя
        User user = converter.fromDto(request);

        Role defaultRole = roleRepository.findByRoleName(roleByDefault)
                .orElseThrow(() -> new NotFoundException("Default role not found in the database"));

        user.setRole(defaultRole);

        LocalDate today = LocalDate.now();

        user.setCreateDate(today);
        user.setLastUpdate(today);

        User savedUser = repository.save(user);

        UserResponseDto response = converter.toDto(savedUser);

        return  response;
    }


    private void makeRequestValidation(UserRequestDto request) {
        List<String> validationErrors = userValidation.validate(request);

        if (!validationErrors.isEmpty()) {
            String errorMessage = "";
            for (String currentError : validationErrors) {
                errorMessage = errorMessage + "\n" + currentError;
            }
            throw new ValidationException(errorMessage);
        }

    }

    private void makeAlreadyExistEmail(String email) {
        Optional<User> userByEmailOptional = repository.findByEmail(email);

        if (userByEmailOptional.isPresent()) {
            throw new AlreadyExistException("Пользователь с email: " + email + " уже существует");
        }
    }


    public UserResponseDto updateUser(UserUpdateRequestDto updateRequest){

        Optional<User> userForUpdateOptional = repository.findById(updateRequest.getId());

        if (userForUpdateOptional.isEmpty()) {
           throw new NotFoundException("Пользователь с id = " + updateRequest.getId() + " не найден");
        }

        User userForUpdate = userForUpdateOptional.get();

        if (updateRequest.getUserName() != null) {
            userForUpdate.setUserName(updateRequest.getUserName());
        }

        if (updateRequest.getPassword() != null) {
            userForUpdate.setPassword(updateRequest.getPassword());
        }

        userForUpdate.setLastUpdate(LocalDate.now());

        repository.save(userForUpdate);

        return  converter.toDto(userForUpdate);

    }



    public List<User> getAllUsersAdmin(){
        return  repository.findAll();
    }


    public List<UserResponseDto> getAll() {


        return converter.toDtos(repository.findAll());

    }


    public UserResponseDto getUserById(Integer id){

        return repository.findById(id)
                .map(user -> converter.toDto(user))
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));

    }


    public Optional<User> getUserByIdForTaskService(Integer id){
        Optional<User> userByIdOptional = repository.findById(id);

        if (userByIdOptional.isPresent()) {
            return userByIdOptional;
        } else {
            return Optional.empty();
        }
    }

    public UserResponseDto getUserByEmail(String email){

        return repository.findByEmail(email)
                .map(user -> converter.toDto(user))
                .orElseThrow(() -> new NotFoundException("Пользователь с email:  " + email + " не найден"));

    }


    public List<UserResponseDto> getUserByRole(String role){

        Role userRole = roleRepository.findByRoleName(role)
                .orElseThrow((() -> new NotFoundException("Роль " + role + " не определена в системе")));

        List<User> usersByRole = repository.findByRole(userRole);

        return converter.toDtos(usersByRole);
    }

    public List<UserResponseDto> getUserByUsername(String username){

        List<User> usersByUsername= repository.findByUserName(username);

        return converter.toDtos(usersByUsername);
    }

    public UserResponseDto deleteUser(Integer id){

        return repository.findById(id)
                .map(user -> {
                    repository.deleteById(id);
                    return converter.toDto(user);
                })
                .orElseThrow( () -> new NotFoundException("Пользователь с id = " + id + " не найден"));
  }

}
