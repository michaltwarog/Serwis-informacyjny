package com.editorial.controller;

import com.editorial.model.dto.UserRegistrationDto;
import com.editorial.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.editorial.util.AccountConstants.APP_SUPPLIER;

@RestController
@RequestMapping("/editorial")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        return ResponseEntity.ok("Correct registration process.");
    }

    @PostMapping("/registration/fc")
    public ResponseEntity<String> createUserAccountByClient(@Valid @RequestBody UserRegistrationDto userRegistrationDto, @RequestHeader("X-Caller") String caller) {
        return ResponseEntity.ok("Correct registration process.");
    }

    @PostMapping("/validate")
    public ResponseEntity<String> enableUserFromClient(@RequestBody Long userId, @RequestHeader("X-Caller") String caller) {
        return registerService.enableUser(userId);
    }
}