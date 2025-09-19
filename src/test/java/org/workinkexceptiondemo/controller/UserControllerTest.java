package org.workinkexceptiondemo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.workinkexceptiondemo.dto.UserRequestDto;
import org.workinkexceptiondemo.dto.UserResponseDto;
import org.workinkexceptiondemo.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testCreateUserSuccess() throws Exception {
        // подготовка

        UserResponseDto response = new UserResponseDto(1,"Ruslan","ruslan@company.com","USER");

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(response);

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                 "userName":"Ruslan",
                                 "email": "ruslan@company.com",
                                 "password": "Pass12345"                               
                                 }    
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userName").value("Ruslan"))
                .andExpect(jsonPath("$.email").value("ruslan@company.com"))
                .andExpect(jsonPath("$.role").value("USER")

        );

    }

    @Test
    void testGetAllUsers() throws Exception {

        List<UserResponseDto> responses = new ArrayList<>();
        responses.add(new UserResponseDto(1,"Ruslan","ruslan@company.com","USER"));
        responses.add(new UserResponseDto(2,"Olga","olga@company.com","USER"));


        when(userService.getAll()).thenReturn(responses);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("Ruslan"))
                .andExpect(jsonPath("$[1].userName").value("Olga"));
    }

}