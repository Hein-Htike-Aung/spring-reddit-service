package com.example.redditservice;

import org.testcontainers.containers.MySQLContainer;

/*
* Use TestContainer through Docker
* */
public abstract class BaseTest {

    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("spring-reddit-test-db")
            .withUsername("testuser")
            .withPassword("pass")
            .withReuse(true);

    static {
        mySQLContainer.start();
    }
}