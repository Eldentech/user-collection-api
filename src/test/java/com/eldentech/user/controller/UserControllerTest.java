package com.eldentech.user.controller;

import com.eldentech.user.domain.enity.Sex;
import com.eldentech.user.dto.UserDTO;
import com.eldentech.user.exceptions.ErrorOnListingUsers;
import com.eldentech.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest({UserController.class})
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;
    private static final String BASE_API = "/api/v1/users";

    public UserDTO createUser(String id, String name) {
        return new UserDTO(id, name, Sex.MALE, 39, "TUR");
    }

    @Test
    void userController_whenRequestedForUsers_shouldReturnUsers() {
        Flux<UserDTO> userList = Flux.range(0, 10)
                .map(k -> createUser(UUID.randomUUID().toString(), "Test " + k));
        when(userService.getUserList()).thenReturn(userList);
        List<UserDTO> users = webTestClient
                .get()
                .uri(BASE_API + "/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UserDTO.class)
                .hasSize(10)
                .returnResult()
                .getResponseBody();

        assertThat(users).isNotNull();
    }

    @Test
    void userController_whenRequestedForUsersWhenExceptionOccurs_shouldReturnError()  {
        when(userService.getUserList()).thenReturn(Flux.error(new ErrorOnListingUsers()));
        webTestClient
                .get()
                .uri(BASE_API + "/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void userController_whenRequestedForUnknownUserExceptionOccurs_shouldReturnError()  {
        webTestClient
                .get()
                .uri(BASE_API + "/not_valid")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void userController_whenRequestedForAdd_shouldAdd()  {
        UserDTO request = new UserDTO(
                null,
                "Rifat Dover",
                Sex.MALE,
                39,
                "TUR"

        );

        UserDTO response = new UserDTO(
                UUID.randomUUID().toString(),
                "Rifat Dover",
                Sex.MALE,
                39,
                "TUR"

        );
        when(userService.addUser(request)).thenReturn(Mono.just(response));
        UserDTO user = webTestClient
                .post()
                .uri(BASE_API + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(user).isNotNull();
        assertThat(user.id()).isNotBlank();
        verify(userService, times(1)).addUser(request);
    }

    @Test
    void userController_whenRequestedForInvalidAdd_shouldReturnError()  {
        UserDTO request = new UserDTO(
                null,
                "Rifat Dover".repeat(70),
                Sex.MALE,
                39,
                "TUR"

        );

        webTestClient
                .post()
                .uri(BASE_API + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest();

        verify(userService, times(0)).addUser(request);
    }


}
