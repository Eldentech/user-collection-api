package com.eldentech.user.configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.eldentech.user.domain")
public class DatabaseConfiguration {

}
