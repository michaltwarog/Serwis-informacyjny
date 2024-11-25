package com.editorial.controller;

import com.editorial.model.dto.UserDto;
import com.editorial.model.dto.UserEditDto;
import com.editorial.model.entity.User;
import com.editorial.service.BasicServiceImpl;
import com.editorial.service.UserActionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/editorial/actions")
public class UserActionController {
    private final UserActionService userActionService;
    private final BasicServiceImpl basicService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserActionController(UserActionService userActionService, BasicServiceImpl basicService, PasswordEncoder passwordEncoder) {
        this.userActionService = userActionService;
        this.basicService = basicService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/get/users")
    public ResponseEntity<List<UserDto>> getUsersInfoPaged(Pageable pageable,
                                                           @RequestParam(name = "role", required = false) String role,
                                                           @RequestParam(name = "attributeName", required = false) String attributeName,
                                                           @RequestParam(name = "attributeValue", required = false) String attributeValue) {
            return userActionService.findAllUsersPaged(pageable);
    }

    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.builder().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam(name = "id") Long userId, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/fc")
    public ResponseEntity<String> deleteUserForClient(@RequestParam(name = "id") Long userId, @RequestHeader("X-Caller") String caller) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam(name = "id") Long userId, @Valid @RequestBody UserEditDto userEditDto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insufficient privileges - you are neither admin nor account owner!");
    }

    @PutMapping("/edit/fc")
    public ResponseEntity<String> editUserForClient(@RequestParam(name = "id") Long userId,
                                                    @RequestParam(name = "loggedId") Long loggedUserId,
                                                    @Valid @RequestBody UserEditDto userEditDto,
                                                    @RequestHeader("X-Caller") String caller) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
