package com.pedro.avaliacaospringapi.controllers;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.avaliacaospringapi.models.User;
import com.pedro.avaliacaospringapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Usuários", description = "Endpoints para usuários")
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @Operation(summary = "Retornar todos os usuários")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("")
    @Operation(summary = "Cadastrar um novo usuário")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um usuário por id")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    @Operation(summary = "Procurar usuários por nome")
    public ResponseEntity<?> findByName(@RequestParam String name) throws NameNotFoundException {
        List<User> users = userService.getUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Autalizar usuário por id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    User updated = userService.updateUser(id, user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(updated);
}
}
