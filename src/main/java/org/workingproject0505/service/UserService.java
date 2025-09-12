package org.workingproject0505.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject0505.dto.GeneralResponse;
import org.workingproject0505.dto.UserRequestDto;
import org.workingproject0505.dto.UserResponseDto;
import org.workingproject0505.dto.UserUpdateRequestDto;
import org.workingproject0505.entity.Role;
import org.workingproject0505.entity.User;
import org.workingproject0505.repository.UserRepository;
import org.workingproject0505.service.util.UserConverter;
import org.workingproject0505.service.validation.UserValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserConverter converter;
    private final UserValidation userValidation;

    public GeneralResponse<UserResponseDto> createUser(UserRequestDto request){

        List<String> validationErrors = userValidation.validate(request);

        if (!validationErrors.isEmpty()) {
            String errorMessage = "";
            for (String currentError : validationErrors) {
                errorMessage = errorMessage + "\n" + currentError;
            }
            return new GeneralResponse<>(HttpStatus.BAD_REQUEST, null, errorMessage);
        }

        Optional<User> userByEmailOptional = repository.findByEmail(request.getEmail());

        if (userByEmailOptional.isPresent()) {
            return new GeneralResponse<>(HttpStatus.BAD_REQUEST, null, "Пользователь с email: " + request.getEmail() + " уже существует");
        }

        User user = converter.fromDto(request);
        user.setRole(Role.USER);

        LocalDate today = LocalDate.now();

        user.setCreateDate(today);
        user.setLastUpdate(today);

        User savedUser = repository.save(user);

        UserResponseDto response = converter.toDto(savedUser);



        return new GeneralResponse<>(HttpStatus.CREATED, response, "Новый пользователь успешно создан");
    }



    public GeneralResponse<UserResponseDto> updateUser(UserUpdateRequestDto updateRequest){

        Optional<User> userForUpdateOptional = repository.findById(updateRequest.getId());

        if (userForUpdateOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователь с id = " + updateRequest.getId() + " не найден");
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

        return new GeneralResponse<>(HttpStatus.OK, converter.toDto(userForUpdate), "Данные пользователя успешно изменены");

    }



    public GeneralResponse<List<User>> getAllUsersAdmin(){
        return new GeneralResponse<>(HttpStatus.OK, repository.findAll(), "Список всех пользователей (режим администратора)");
    }


    public GeneralResponse<List<UserResponseDto>> getAll() {

//        List<UserResponseDto> response = new ArrayList<>();
//        List<User> users = userRepository.findAll();
//
//        for (User currentUser : users) {
//            UserResponseDto currentResponse = converter.toDto(currentUser);
//            response.add(currentResponse);
//        }
//
//        return response;

        List<UserResponseDto> response = converter.toDtos(repository.findAll());

        String message = "";

        if (response.isEmpty()) {
            message = "Список пользователей пуст";
        } else {
            message = "Список пользователей (режим пользователя)";
        }

        return new GeneralResponse<>(HttpStatus.OK, response, message);
    }


    public GeneralResponse<UserResponseDto> getUserById(Integer id){
        Optional<User> userByIdOptional = repository.findById(id);

        if (userByIdOptional.isPresent()) {
            User userById = userByIdOptional.get();
            UserResponseDto response = converter.toDto(userById);
            return new GeneralResponse<>(HttpStatus.OK, response, "Пользователь найден");
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователь с id = " + id + " не найден");
        }
    }


    public Optional<User> getUserByIdForTaskService(Integer id){
        Optional<User> userByIdOptional = repository.findById(id);

        if (userByIdOptional.isPresent()) {
            return userByIdOptional;
        } else {
            return Optional.empty();
        }
    }

    public GeneralResponse<UserResponseDto> getUserByEmail(String email){
        Optional<User> userByEmailOptional = repository.findByEmail(email);

        if (userByEmailOptional.isPresent()) {
            User userByEmail = userByEmailOptional.get();
            UserResponseDto response = converter.toDto(userByEmail);
            return new GeneralResponse<>(HttpStatus.OK, response, "Пользователь найден");
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователь с email:  " + email + " не найден");
        }
    }


    public GeneralResponse<List<UserResponseDto>> getUserByRole(String role){
        Role userRole = Role.valueOf(role);

        List<User> usersByRole = repository.findByRole(userRole);

        if (!usersByRole.isEmpty()) {
           List<UserResponseDto> response = converter.toDtos(usersByRole);

            return new GeneralResponse<>(HttpStatus.OK, response, "Пользователи с ролью: " + role);
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователи с ролью:  " + role + " не найдены");
        }
    }

    public GeneralResponse<List<UserResponseDto>> getUserByUsername(String username){

        List<User> usersByUsername= repository.findByUserName(username);


        if (!usersByUsername.isEmpty()) {
            List<UserResponseDto> response = converter.toDtos(usersByUsername);

            return new GeneralResponse<>(HttpStatus.OK, response, "Пользователи с именем: " + username);
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователи с именем:  " + username + " не найдены");
        }
    }

    public GeneralResponse<UserResponseDto> deleteUser(Integer id){

        Optional<User> userForDeleteOptional = repository.findById(id);

        if (userForDeleteOptional.isEmpty()){
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователь с id = " + id + " не найден");
        }

        repository.deleteById(id);

        return new GeneralResponse<>(HttpStatus.OK, converter.toDto(userForDeleteOptional.get()), "Пользователь с id = " + id + " успешно удален");
    }

}
