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
        Optional<User> userChecker = userActionService.getLoggedUser();
        if (userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        UserDto userDto = userActionService.getUserInfo(userChecker.get());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam(name = "id") Long userId, HttpServletRequest request) {
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");

        User loggedUser = userChecker.get();
        if (loggedUser.getAuthority().getAuthorityName().equals("ADMIN") || loggedUser.getId().equals(userId)) {
            userActionService.deleteUserById(userId);
            ResponseEntity<String> clientResponse = userActionService.deleteUserEditorialToClient(userId, request);
            if (!clientResponse.getStatusCode().is2xxSuccessful())
                return new ResponseEntity<>(clientResponse.getStatusCode());
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/fc")
    public ResponseEntity<String> deleteUserForClient(@RequestParam(name = "id") Long userId, @RequestHeader("X-Caller") String caller) {
        if (!"DELETE_FROM_CLIENT".equals(caller))
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
        Optional<User> userChecker = userActionService.getLoggedUser();

        if (userChecker.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username of requesting user does not exist in db!");

        User loggedUser = userChecker.get();

        if (!loggedUser.getAuthority().getAuthorityName().equals("ADMIN")
                && (userEditDto.getPasswordToConfirm() == null || !passwordEncoder.matches(userEditDto.getPasswordToConfirm(), loggedUser.getPassword())) && !loggedUser.getUserDetails().getSupplier().equals("GOOGLE"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insufficient privileges - you are neither admin nor provided the right password!");

        if (loggedUser.getAuthority().getAuthorityName().equals("ADMIN") || loggedUser.getId().equals(userId)) {
            User userToEdit = userActionService.findUserById(userId);
            if (!userToEdit.getUsername().equals(userEditDto.getUsername())
                    && userActionService.findUserByUsername(userEditDto.getUsername()) != null)
                return ResponseEntity.badRequest().body("Username already exists in db!");
            userActionService.updateUser(userToEdit, userEditDto, loggedUser.getId());
            ResponseEntity<String> clientResponse = userActionService.updateUserEditorialToClient(userId, loggedUser.getId(), userEditDto, request);
            if (!clientResponse.getStatusCode().is2xxSuccessful())
                return ResponseEntity.badRequest().body("Bad request from client side - editorial");
            return ResponseEntity.ok("Successfully edited user");
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Insufficient privileges - you are neither admin nor account owner!");
    }

    @PutMapping("/edit/fc")
    public ResponseEntity<String> editUserForClient(@RequestParam(name = "id") Long userId,
                                                    @RequestParam(name = "loggedId") Long loggedUserId,
                                                    @Valid @RequestBody UserEditDto userEditDto,
                                                    @RequestHeader("X-Caller") String caller) {
        if (!"EDIT_FROM_CLIENT".equals(caller))
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
