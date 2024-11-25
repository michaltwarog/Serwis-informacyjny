package com.client.controller;

import com.client.model.dto.UserEditDto;
import com.client.model.dto.UserDto;
import com.client.model.dto.UserRegistrationDto;
import com.client.model.entity.User;
import com.client.service.BasicServiceImpl;
import com.client.service.UserActionService;
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
@RequestMapping("/client/actions")
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
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of());

        if (role != null && attributeName != null)
            return userActionService.findAllUsersByAttributeNameAndRolePaged(pageable, role, attributeName, attributeValue);
        else if (role != null)
            return userActionService.findAllUsersByRolePaged(pageable, role);
        else if (attributeName != null) {
            return userActionService.findAllUsersByAttributeNamePaged(pageable, attributeName, attributeValue);
        } else
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

    @DeleteMapping("/delete/fe")
    public ResponseEntity<String> deleteUserForEditorial(@RequestParam(name = "id") Long userId, @RequestHeader("X-Caller") String caller) {
        if (!"DELETE_FROM_EDITORIAL".equals(caller))
            return ResponseEntity.badRequest().build();
        else {
            try {
                userActionService.deleteUserById(userId);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam(name = "id") Long userId, @Valid @RequestBody UserEditDto userEditDto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insufficient privileges - you are neither admin nor account owner!");
    }

    @PutMapping("/edit/fe")
    public ResponseEntity<String> editUserForEditorial(@RequestParam(name = "id") Long userId,
                                                       @RequestParam(name = "loggedId") Long loggedUserId,
                                                       @Valid @RequestBody UserEditDto userEditDto,
                                                       @RequestHeader("X-Caller") String caller) {
        if (!"EDIT_FROM_EDITORIAL".equals(caller))
            return ResponseEntity.badRequest().build();
        else {
            try {
                User userToEdit = userActionService.findUserById(userId);
                userActionService.updateUser(userToEdit, userEditDto, loggedUserId);
                return ResponseEntity.ok().build();
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}
