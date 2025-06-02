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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.avaliacaospringapi.models.Category;
import com.pedro.avaliacaospringapi.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias", description = "Endpoints para categorias")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    @Operation(summary = "Retornar todas as categorias")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }

    @PostMapping("")
    @Operation(summary = "Cadastrar uma nova cateogoria")
    public Category addCategory(@Valid @RequestBody Category categorie) {
        return categoryService.addCategory(categorie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar uma categoria por id")
    public ResponseEntity<Void> deleteCategoryById(@Valid @PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    @Operation(summary = "Procurar categorias por nome")
    public ResponseEntity<?> findByName(@RequestParam String name) {
        List<Category> categories = categoryService.getCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma categoria por id")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoria) {
    Category updated = categoryService.updateCategory(id, categoria);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(updated);
    }
}
