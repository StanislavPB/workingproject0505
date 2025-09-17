package org.workinkexceptiondemo.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.workinkexceptiondemo.dto.UserRequestDto;
import org.workinkexceptiondemo.dto.UserResponseDto;
import org.workinkexceptiondemo.dto.UserUpdateRequestDto;
import org.workinkexceptiondemo.entity.User;
import org.workinkexceptiondemo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public interface UserControllerInterface {


    // Создать пользователя
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto request);

    // Получить всех пользователей (пользовательский режим)
    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllForAdmin();


    // Получить пользователя по id
    @Operation(summary = "Получить пользователя", description = "Возвращает информацию о пользователе по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Integer id);


    // Обновить пользователя по id (на основе данных из UserUpdateRequestDto)
    @PutMapping("/{id}")
    public UserResponseDto update(@RequestBody UserUpdateRequestDto updateRequest);


    // найти пользователя по email
    // /api/users?email=...
    // /api/users?username=...

    @GetMapping()
    public List<UserResponseDto> getAllUsersByParameter(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "username", required = false) String username
    );


}
