package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.entity.User;
import com.webhook.whatsapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserService userService;

    //for getting all users
    @Operation(summary = "This is to fetch all the users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //for getting users by id
    @Operation(summary = "This is to fetch users by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    //for adding user
    @Operation(summary = "This is to create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    //for modifying user
    @Operation(summary = "This is to modify user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User user) {
        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return null;
        }
        // update existing user fields with the values from the request body
        existingUser.setUsername(user.getUsername());
        return userService.saveUser(existingUser);
    }

    @Operation(summary = "This is to delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }



}


