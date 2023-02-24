package com.eldentech.user.service;

import com.eldentech.user.domain.enity.User;
import com.eldentech.user.domain.repository.UserRepository;
import com.eldentech.user.dto.UserDTO;
import com.eldentech.user.exceptions.ErrorOnListingUsers;
import com.eldentech.user.exceptions.ErrorOnSavingUser;
import com.eldentech.user.exceptions.UserNotFoundError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<UserDTO> getUserList() {
        return Mono.fromCallable(() -> userRepository.findAll())
                .flux()
                .flatMap(Flux::fromIterable)
                .map(UserService::from)
                .doOnError(throwable -> logger.error("Error on saving user", throwable))
                .onErrorResume(throwable -> Flux.error(new ErrorOnListingUsers()));
    }

    public Mono<UserDTO> getUserById(String id) {
        return Mono.fromCallable(() -> userRepository.findById(id))
                .map(optional -> optional.map(UserService::from).orElseThrow(() -> new UserNotFoundError(id)))
                .doOnError(throwable -> logger.error("Error on getting user: %s".formatted(id), throwable))
                .onErrorResume(throwable -> {
                    if (throwable instanceof UserNotFoundError) return Mono.error(throwable);
                    return Mono.error(new ErrorOnListingUsers());
                });
    }

    public Mono<UserDTO> addUser(UserDTO userDTO) {
        return Mono.fromCallable(() -> userRepository.save(from(userDTO)))
                .map(UserService::from)
                .doOnError(throwable -> logger.error("Error on saving user", throwable))
                .onErrorResume(throwable -> Mono.error(new ErrorOnSavingUser()));
    }

    public static User from(UserDTO userDTO) {
        return new User(
                userDTO.id(),
                userDTO.name(),
                userDTO.sex(),
                userDTO.age(),
                userDTO.country()
        );
    }

    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getSex(),
                user.getAge(),
                user.getCountry()
        );
    }
}
