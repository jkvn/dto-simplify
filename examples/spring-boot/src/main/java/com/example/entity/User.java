package com.example.entity;


import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.annotation.DtoSchema;

import java.util.List;

@DtoSchema(value = "admin", description = "Admin user schema")
public class User {
    @Dto("admin")
    private String username;
    @Dto("admin")
    private Test testData;
    @Dto("admin")
    private List<Test> testList = List.of(
            new Test("test1"),
            new Test("test2")
    );
    @Dto("admin")
    private List<String> strings = List.of("a", "b", "c");

    public User(String username, Test testData) {
        this.username = username;
        this.testData = testData;
    }

    public String getUsername() {
        return username;
    }

    public Test getTestData() {
        return testData;
    }
}