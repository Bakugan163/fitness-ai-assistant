package com.mityaev.fitnessai.users.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Импортируем наш новый сервис
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mityaev.fitnessai.users.dto.CreateUserRequest;
import com.mityaev.fitnessai.users.dto.UserProfileResponse;
import com.mityaev.fitnessai.users.entity.User;
import com.mityaev.fitnessai.users.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService; // Теперь контроллер знает только о Сервисе!

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        // Контроллер просто делегирует задачу сервису. 
        // Ему плевать, как именно создается юзер. Его дело - принять запрос и отдать ответ.
        User savedUser = userService.createUser(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

               @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        UserProfileResponse userResponse = userService.getCurrentUser(principal.getName());
        return ResponseEntity.ok(userResponse);
    }
        


    
}