package com.mityaev.fitnessai.profiles.controllers;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mityaev.fitnessai.profiles.dto.ClientProfileResponse;
import com.mityaev.fitnessai.profiles.dto.CreateClientProfileRequest;
import com.mityaev.fitnessai.profiles.service.ClientProfileService;

@RestController
@RequestMapping("/api/v1/profiles")
public class ClientProfileController {

    private final ClientProfileService profileService;

    public ClientProfileController(ClientProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ClientProfileResponse> createProfile(
            @RequestBody CreateClientProfileRequest request, 
            Principal principal) { 
        // Principal - это наш юзер из JWT токена. Контроллер просто передает его email в сервис.
        // Никакой логики! Только делегирование.
        ClientProfileResponse response = profileService.createProfile(request, principal.getName());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}