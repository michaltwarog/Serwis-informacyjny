package com.client.controller;

import com.client.model.dto.UserRegistrationDto;
import com.client.service.LoginService;
import com.client.service.RegisterService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

import static com.client.util.AccountConstants.APP_SUPPLIER;
import static com.client.util.AccountConstants.ROLE_USER;

@RestController
@RequestMapping("/client")
public class RegisterController {

    private final LoginService loginService;
    private final RegisterService registerService;

    @Autowired
    public RegisterController(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        return ResponseEntity.ok("Correct registration process.");
    }

    @PostMapping("/registration/fe")
    public ResponseEntity<String> createUserAccountByEditorial(@Valid @RequestBody UserRegistrationDto userRegistrationDto, @RequestHeader("X-Caller") String caller) {
            return ResponseEntity.ok("Correct registration process.");
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestParam("code") String code, HttpServletRequest request) {
        return registerService.validateCode(code, request);
    }
}
