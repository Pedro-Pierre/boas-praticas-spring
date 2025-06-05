package com.pedro.avaliacaospringapi.controllers;

import com.pedro.avaliacaospringapi.config.exception.notFound.NameNotFoundException;
import com.pedro.avaliacaospringapi.models.Product;
import com.pedro.avaliacaospringapi.models.User;
import com.pedro.avaliacaospringapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Usuários", description = "Endpoints para usuários")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @Operation(summary = "Retornar todos os usuários")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("")
    @Operation(summary = "Cadastrar um novo usuário")
    public User addUser(@RequestBody @Valid User user) {
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
    public ResponseEntity<List<User>> findByName(@RequestParam String name) throws NameNotFoundException {
        List<User> users = userService.getUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário por id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    // --- Endpoints para o relacionamento de User com Product ---

    @GetMapping("/{userId}/products")
    @Operation(summary = "Listar produtos de um usuário")
    public ResponseEntity<Set<Product>> listarProdutos(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.listarProdutosDoUsuario(userId));
    }

    @PostMapping("/{userId}/products/{productId}")
    @Operation(summary = "Adicionar um produto ao usuário")
    public ResponseEntity<Void> adicionarProduto(@PathVariable Long userId, @PathVariable Long productId) {
        userService.adicionarProdutoAoUsuario(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/products/{productId}")
    @Operation(summary = "Remover um produto do usuário")
    public ResponseEntity<Void> removerProduto(@PathVariable Long userId, @PathVariable Long productId) {
        userService.removerProdutoDoUsuario(userId, productId);
        return ResponseEntity.ok().build();
    }

}
