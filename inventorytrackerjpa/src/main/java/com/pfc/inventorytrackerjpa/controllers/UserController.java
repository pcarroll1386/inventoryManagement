package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.dto.UserCreateRequest;
import com.pfc.inventorytrackerjpa.dto.UserUpdateRequest;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import com.pfc.inventorytrackerjpa.entities.User;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.InvalidUserException;
import com.pfc.inventorytrackerjpa.services.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/getById/{id}")
    public User getById(@PathVariable("id") long id) throws InvalidUserException {
        return userService.getById(id);
    }

    @GetMapping("/getByUsername/{username}")
    public User getByUsername(@PathVariable("username") String username) throws InvalidUserException {
        return userService.getByUsername(username);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserCreateRequest request) throws InvalidRoleException, InvalidUserException {
        return userService.createUser(request);


    }

    @PatchMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User patchUser(@PathVariable("id") long id, @Valid @RequestBody UserUpdateRequest request)
            throws InvalidDataException, InvalidRoleException, InvalidUserException {
        return userService.updateUser(id, request);
    }

    // When auth gets implimented this needs to be tied down to Super Auth user. use disable user instead.
    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable("id") long id) throws InvalidUserException {
        userService.deleteUser(id);
    }
    
}
