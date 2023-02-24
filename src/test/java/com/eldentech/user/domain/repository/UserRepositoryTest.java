package com.eldentech.user.domain.repository;

import com.eldentech.user.domain.enity.Sex;
import com.eldentech.user.domain.enity.User;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Test
    public void userRepository_whenRequestedAllUsers_shouldGet() {
        List<User> users = userRepository.findAll();
        assertThat(users).isNotNull();
        assertThat(users.isEmpty()).isFalse();
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    public void userRepository_whenUserInserted_shouldInsertToDatabase() {
        User user = createUser("Test User", "TUR");
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotBlank();
        assertThat(user.getName()).isEqualTo("Test User");
    }

    private User createUser(String name, String country) {
        User user = userRepository.save(new User(name, Sex.MALE, 39, country));
        userRepository.flush();
        return user;
    }

    @Test
    public void userRepository_whenUserDeleted_shouldDeleteFromDatabase() {
        String name = "My User";
        createUser(name, "TUR");
        Example<User> userExample = Example.of(new User(name, Sex.MALE, 39, "TUR"));
        Optional<User> optionalUser = userRepository
                .findOne(userExample);

        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();
        assertThat(user.getId()).isNotBlank();
        userRepository.delete(user);
        userRepository.flush();

        assertThat(userRepository.findOne(userExample)).isNotPresent();
    }

    @Test
    public void userRepository_whenUserUpdated_shouldUpdateFromDatabase() {
        User user = createUser("First User", "TUR");
        assertThat(user).isNotNull();
        user.setName("Updated User");
        userRepository.save(user);
        userRepository.flush();

        Optional<User> updated = userRepository.findById(user.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Updated User");
    }

    @Test
    public void userRepository_whenUserFailsConstraint_shouldFailInsertToDatabase() {
        String longName = "Long User".repeat(70);
        assertThatThrownBy(() -> createUser(longName, "TUR"))
                .hasCauseInstanceOf(DataException.class);

        assertThatThrownBy(() -> createUser("Normal Name", ""))
                .hasCauseInstanceOf(DataException.class);
    }

}
