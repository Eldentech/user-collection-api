package com.eldentech.user.service;

import com.eldentech.user.domain.enity.Sex;
import com.eldentech.user.domain.enity.User;
import com.eldentech.user.domain.repository.UserRepository;
import com.eldentech.user.dto.UserDTO;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    UserRepository repository = Mockito.mock(UserRepository.class);
    UserService userService = new UserService(repository);

    public User createUser(String name) {
        return new User(name, Sex.MALE, 39, "TUR");
    }

    @Test
    void userService_whenGetUsersCalled_shouldReturnUsers() {
        List<User> mockUsers = Stream.of("One", "Two", "Three").map(this::createUser).collect(Collectors.toList());
        Mockito.when(repository.findAll()).thenReturn(mockUsers);
        StepVerifier.create(userService.getUserList())
                .expectNext(
                        UserService.from(mockUsers.get(0)),
                        UserService.from(mockUsers.get(1)),
                        UserService.from(mockUsers.get(2))
                )
                .expectComplete()
                .verify();
    }

    @Test
    void userService_whenGetUsersReceivedAnException_shouldReturnErrorOnListingUsers() {
        Mockito.when(repository.findAll()).thenThrow(DataException.class);
        StepVerifier.create(userService.getUserList())
                .expectErrorMessage("500 INTERNAL_SERVER_ERROR \"Unexpected error occurred while listing users\"")
                .verify();
    }

    @Test
    void userService_whenGetUserByIdCalled_shouldReturnUser() {
        User testUser = createUser("Test User");
        Mockito.when(repository.findById(any())).thenReturn(Optional.of(testUser));
        StepVerifier.create(userService.getUserById("aaa"))
                .expectNext(UserService.from(testUser))
                .expectComplete()
                .verify();
    }

    @Test
    void userService_whenGetUserByIdCalledForNotExistingUser_shouldReturnNotFound() {
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());
        StepVerifier.create(userService.getUserById("aaa"))
                .expectErrorMessage("404 NOT_FOUND \"User can not be found with id:aaa\"")
                .verify();
    }

    @Test
    void userService_whenGetUserByIdCalledWithError_shouldReturnErrorOnListingUsers() {
        Mockito.when(repository.findById(any())).thenThrow(DataException.class);
        StepVerifier.create(userService.getUserById("aaa"))
                .expectErrorMessage("500 INTERNAL_SERVER_ERROR \"Unexpected error occurred while listing users\"")
                .verify();
    }

    @Test
    void userService_whenAddUser_shouldReturnErrorOnSavingUser() {
        Mockito.when(repository.save(any())).thenThrow(DataException.class);
        StepVerifier.create(userService.addUser(new UserDTO("aaa", "aaa", Sex.MALE, 35, "TUR")))
                .expectErrorMessage("500 INTERNAL_SERVER_ERROR \"Unexpected error occurred while adding user\"")
                .verify();
    }
}
