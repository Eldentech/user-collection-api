package com.eldentech.user.domain.repository;

import com.eldentech.user.domain.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
