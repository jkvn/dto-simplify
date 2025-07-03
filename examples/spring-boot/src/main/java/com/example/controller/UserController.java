package com.example.controller;

import at.jkvn.dtosimplify.core.api.ViewBuilder;
import com.example.entity.Test;
import com.example.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @GetMapping("/user/admin")
    public Object getAdminUser() {
        User user = new User("kevin", new Test("internalId123"));
        return new ViewBuilder(user).as("admin").map();
    }
}