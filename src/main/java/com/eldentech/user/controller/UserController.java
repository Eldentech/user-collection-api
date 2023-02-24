package com.eldentech.user.controller;

import com.eldentech.user.dto.UserDTO;
import com.eldentech.user.service.UserService;
import com.eldentech.user.validation.UUIDValidation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public Mono<UserDTO> getUser(@PathVariable @UUIDValidation String userId) {
        return this.userService.getUserById(userId);
    }

    @GetMapping("/")
    public Flux<UserDTO> getUsers() {
        return this.userService.getUserList();
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) {
        return this.userService.addUser(userDTO);
    }
}
