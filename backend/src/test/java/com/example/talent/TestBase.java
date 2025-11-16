package com.example.talent;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(classes = TalentManagementApplication.class)
public abstract class TestBase {

    private static final MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("talent_test")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);
        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }
}
