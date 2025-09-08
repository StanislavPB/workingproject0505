package org.workingproject0505.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workingproject0505.dto.GeneralResponse;
import org.workingproject0505.dto.UserRequestDto;
import org.workingproject0505.dto.UserResponseDto;
import org.workingproject0505.dto.UserUpdateRequestDto;
import org.workingproject0505.entity.User;
import org.workingproject0505.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    // Создать пользователя
    @PostMapping
    public GeneralResponse<UserResponseDto> create(@RequestBody UserRequestDto request){
        return service.createUser(request);
    }


    // Получить всех пользователей (пользовательский режим)
    @GetMapping
    public GeneralResponse<List<UserResponseDto>> getAll(){
        return service.getAll();
    }

    // Получить всех пользователей (пользовательский режим)
    @GetMapping("/admin")
    public GeneralResponse<List<User>> getAllForAdmin(){
        return service.getAllUsersAdmin();
    }


    // Получить пользователя по id
    @GetMapping("/{id}")
    public GeneralResponse<UserResponseDto> getById(@PathVariable Integer id) {
        return service.getUserById(id);
    }


    // Обновить пользователя по id (на основе данных из UserUpdateRequestDto)
    @PutMapping("/{id}")
    public GeneralResponse<UserResponseDto> update(@RequestBody UserUpdateRequestDto updateRequest){
        return service.updateUser(updateRequest);
    }


    // найти пользователя по email
    // /api/users?email=...
    // /api/users?username=...

    @GetMapping()
    public GeneralResponse<List<UserResponseDto>> getAllUsersByParameter(
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
