package com.example.controller;

import at.jkvn.dtosimplify.core.annotation.schema.SchemaResponse;
import at.jkvn.dtosimplify.core.annotation.schema.SchemaVariant;
import at.jkvn.dtosimplify.core.api.DtoSimplify;
import com.example.entity.Test;
import com.example.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user/admin")
    @SchemaResponse(variants = {
            @SchemaVariant(dto = User.class, profile = "admin"),
            @SchemaVariant(dto = User.class, profile = "user"),
    })
    public Object getAdminUser() {
        User user = new User("kevin", new Test("internalId123"));
        return DtoSimplify.view(user).as("admin").map();
    }

    @GetMapping("/user/list")
    @SchemaResponse(variants = {
            @SchemaVariant(dto = User.class, profile = "admin"),
            @SchemaVariant(dto = User.class, profile = "user"),
    })
    public Object getListUser() {
        List<User> user = List.of(new User("kevin", new Test("internalId123")));
        return DtoSimplify.view(user).as("admin").map();
    }
}