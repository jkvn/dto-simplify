package com.example.entity;


import at.jkvn.dtosimplify.core.annotation.Dto;
import at.jkvn.dtosimplify.core.annotation.DtoSchema;

@DtoSchema(value = "public", description = "Public user schema")
@DtoSchema(value = "admin", description = "Admin user schema")
public class Test {
    @Dto("admin")
    @Dto("public")
    private String username;
    @Dto("admin")
    private String internalId;

    public Test(String username, String internalId) {
        this.username = username;
        this.internalId = internalId;
    }

    public String getUsername() {
        return username;
    }

    public String getInternalId() {
        return internalId;
    }
}