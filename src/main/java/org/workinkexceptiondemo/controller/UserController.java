package org.workinkexceptiondemo.controller;



import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.workinkexceptiondemo.dto.GeneralResponse;
import org.workinkexceptiondemo.dto.UserRequestDto;
import org.workinkexceptiondemo.dto.UserResponseDto;
import org.workinkexceptiondemo.dto.UserUpdateRequestDto;
import org.workinkexceptiondemo.entity.User;
import org.workinkexceptiondemo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    // Создать пользователя
    @PostMapping
    public UserResponseDto create(@RequestBody UserRequestDto request){
        return service.createUser(request);
    }

    // Получить всех пользователей (пользовательский режим)
    @GetMapping("/admin")
    public List<User> getAllForAdmin(){
        return service.getAllUsersAdmin();
    }


    // Получить пользователя по id
    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Integer id) {
        return service.getUserById(id);
    }


    // Обновить пользователя по id (на основе данных из UserUpdateRequestDto)
    @PutMapping("/{id}")
    public UserResponseDto update(@RequestBody UserUpdateRequestDto updateRequest){
        return service.updateUser(updateRequest);
    }


    // найти пользователя по email
    // /api/users?email=...
    // /api/users?username=...

    @GetMapping()
    public List<UserResponseDto> getAllUsersByParameter(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "username", required = false) String username
    ) {

        if (role != null && !role.isBlank()) {
            return service.getUserByRole(role);
        }
        if (username != null && !username.isBlank()){
            return service.getUserByUsername(username);
        }

        return service.getAll();

    }


}
