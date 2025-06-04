package com.pedro.avaliacaospringapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pedro.avaliacaospringapi.config.exception.notFound.IdNotFoundException;
import com.pedro.avaliacaospringapi.config.exception.notFound.NameNotFoundException;
import com.pedro.avaliacaospringapi.models.Category;
import com.pedro.avaliacaospringapi.repositories.CategoryRepository;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IdNotFoundException();
        }
        categoryRepository.deleteById(id);
    }

    public List<Category> getCategoriesByDescription(String description) {
        List<Category> categories = categoryRepository.findByDescription(description);
        if (categories.isEmpty()) throw new NameNotFoundException();
        return categories;
    }

    public Category updateCategory(Long id, Category categoriaAtualizada) {
        Category categoria = categoryRepository.findById(id)
                    .orElseThrow(IdNotFoundException::new);

        categoria.setDescription(categoriaAtualizada.getDescription());

        return categoryRepository.save(categoria);
    }
}
