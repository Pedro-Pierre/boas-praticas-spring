package com.pedro.avaliacaospringapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.pedro.avaliacaospringapi.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM TB_CATEGORIES", nativeQuery = true)
    List<Category> findAll();

    void deleteById(Long id);

    List<Category> findByName(String name);

}

