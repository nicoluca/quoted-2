package org.nico.quoted.controller;

import org.nico.quoted.dto.UuidDto;
import org.nico.quoted.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/uuid") // TODO Check if users can access other users' quotes
    public UuidDto getUserUUID(@RequestParam("email") String email) {
        return new UuidDto(userService.getUserId(email).toString());
    }


}
