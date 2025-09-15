package org.workinkexceptiondemo.service;



import lombok.AllArgsConstructor;
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
import org.workinkexceptiondemo.service.exception.NotFoundException;
import org.workinkexceptiondemo.service.util.UserConverter;
import org.workinkexceptiondemo.service.validation.UserValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// @Service
@AllArgsConstructor
public class UserServiceOldVersion {

    private final UserRepository repository;
    private final UserConverter converter;
    private final UserValidation userValidation;
    private final RoleRepository roleRepository;

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
        Optional<Role> defaultRoleOptional = roleRepository.findByRoleName("USER");

        // Role defRole = new Role("USER"); !!!!! ТАК ДЕЛАТЬ НЕЛЬЗЯ !!!!!
        /*
        1) при создании "нового" объекта вместо ссылки на существующий :
        - new Role - создается новый объект, которого еще нет в базе
        - при сохранении User через repository.save JPA попытается тоже сохранить новую роль, потому что она связана с user

        В результате появятся дубли наших ролей

        -> Потеря согласованности данных
        -> нарушение связей (при поиске "все пользователи с ролью USER == все пользователи с role_id=2")
        -> потенциальные ошибки при маппинге JPA - если поле roleName должно быть уникальным, то при попытке
        сохранить второго пользователя с новой ролью USER произойдет ошибка ConstraintViolationException
         */


        if (defaultRoleOptional.isEmpty()) {
            throw new NotFoundException("Default role not found in the database");
        } else {
            Role defaultRole = defaultRoleOptional.get();
            user.setRole(defaultRole);
        }

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
        Role userRole = roleRepository.findByRoleName(role).get();

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
