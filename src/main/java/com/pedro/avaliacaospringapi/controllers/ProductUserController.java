package com.pedro.avaliacaospringapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.avaliacaospringapi.dtos.ProductUserDto;
import com.pedro.avaliacaospringapi.models.ProductUser;
import com.pedro.avaliacaospringapi.services.ProductUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "ProdutoUsuario", description = "Endpoints para relações de produtos e usuários")
@RestController
@RequestMapping("/products-users")
public class ProductUserController {

    private ProductUserService productUserService;

    public ProductUserController(ProductUserService productUserService) {
        this.productUserService = productUserService;
    }

    @GetMapping("")
    @Operation(summary = "Retornar todas as relações de produto e usuário")
    public List<ProductUser> getAllProductsUsers() {
        return productUserService.getAllProductsUsers();
    } 

    @PostMapping
    @Operation(summary = "Cadastrar uma nova relação de produto e usuário")
    public ResponseEntity<ProductUser> create(@RequestBody ProductUserDto dto) {
        ProductUser created = productUserService.addProductUser(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma relação de produto e usuário por id")
    public ResponseEntity<Void> deleteProductUserById(@Valid @PathVariable Long id) {
        productUserService.deleteProductUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar uma relação de produto e usuário por id")
    public ResponseEntity<ProductUser> update(@PathVariable Long id, @RequestBody ProductUserDto dto) {
        ProductUser updated = productUserService.updateProductUser(id, dto);
        return ResponseEntity.ok(updated);
    }

}
