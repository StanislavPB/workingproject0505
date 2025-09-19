package org.workinkexceptiondemo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.workinkexceptiondemo.dto.UserRequestDto;
import org.workinkexceptiondemo.dto.UserResponseDto;
import org.workinkexceptiondemo.entity.Role;
import org.workinkexceptiondemo.entity.User;
import org.workinkexceptiondemo.repository.RoleRepository;
import org.workinkexceptiondemo.repository.UserRepository;
import org.workinkexceptiondemo.service.exception.AlreadyExistException;
import org.workinkexceptiondemo.service.exception.NotFoundException;
import org.workinkexceptiondemo.service.util.UserConverter;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private UserConverter userConverter;

    @InjectMocks UserService userService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        // вручную ставим значение в приватное поле
//        Field field = UserService.class.getDeclaredField("roleByDefault");
//        field.setAccessible(true);
//        field.set(userService, "USER");
    }

    @Test
    void testCreateUserSuccess(){
        // подготовительные действия
        UserRequestDto request = new UserRequestDto("Ruslan","ruslan@company.com","Pass12345");
        Role defaultRole = new Role(2, "USER");

        LocalDate now = LocalDate.now();

        User userFromDto = new User(null, request.getUserName(), request.getEmail(), request.getPassword(), null, now, now, null);
        User savedUser = new User(1, request.getUserName(), request.getEmail(), request.getPassword(), defaultRole, now, now, null);

        UserResponseDto response = new UserResponseDto(1,"Ruslan","ruslan@company.com", "USER");


        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userConverter.fromDto(request)).thenReturn(userFromDto);
        when(roleRepository.findByRoleName(any(String.class))).thenReturn(Optional.of(defaultRole));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userConverter.toDto(any(User.class))).thenReturn(response);


        // тест

        UserResponseDto testResult = userService.createUser(request);

        assertEquals("Ruslan", testResult.getUserName());
        assertEquals("ruslan@company.com", testResult.getEmail());
        assertEquals(1, testResult.getId());
        assertEquals("USER", testResult.getRole());

        verify(userRepository).save(any(User.class));

    }

    @Test
    void testCreateUserAlreadyExist(){
        // подготовительные действия
        UserRequestDto request = new UserRequestDto("Ruslan","ruslan@company.com","Pass12345");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(AlreadyExistException.class, () -> userService.createUser(request));

    }

    @Test
    void testGetUserByIdReturnNotFountException() {

        Integer searchId = 1;

        when(userRepository.findById(searchId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(searchId));
    }

}