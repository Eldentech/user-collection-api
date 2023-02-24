package com.eldentech.user.controller;

import com.eldentech.user.domain.enity.Sex;
import com.eldentech.user.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {
    private static final String BASE_API = "/api/v1/users";
    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void api_whenRequestedForUsersAndUser_shouldListUsersAndUser() {
        List<UserDTO> users = webTestClient
                .get()
                .uri(BASE_API + "/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UserDTO.class)
                .hasSize(4)
                .returnResult()
                .getResponseBody();

        assertThat(users).isNotNull();

        webTestClient
                .get()
                .uri(BASE_API + "/" + users.get(0).id())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDTO.class);

    }
    @Test
    public void api_whenRequestedForNotExistingUser_shouldReturnNotFound() {
        webTestClient
                .get()
                .uri(BASE_API + "/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void api_whenRequestedForNotValidUserId_shouldReturnError() {
        webTestClient
                .get()
                .uri(BASE_API + "/" + "not_valid")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    public void api_whenRequestedForPostUser_shouldAddUser() {
        UserDTO user = webTestClient
                .post()
                .uri(BASE_API + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new UserDTO(
                        null,
                        "Rifat Dover",
                        Sex.MALE,
                        39,
                        "TUR"

                )), UserDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(user).isNotNull();
        assertThat(user.id()).isNotBlank();

    }
}
