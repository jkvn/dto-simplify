package com.example.entity;


import at.jkvn.dtosimplify.core.annotation.response.Dto;
import at.jkvn.dtosimplify.core.annotation.schema.DtoSchema;

import java.time.LocalDateTime;

@DtoSchema(value = "admin", description = "Admin test schema")
public class Test {
    @Dto("admin")
    private String internalId;
    @Dto("admin")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Test(String internalId) {
        this.internalId = internalId;
    }

    public String getInternalId() {
        return internalId;
    }
}