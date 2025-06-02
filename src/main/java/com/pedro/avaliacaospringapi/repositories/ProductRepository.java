package com.pedro.avaliacaospringapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pedro.avaliacaospringapi.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM TB_PRODUCTS", nativeQuery = true)
    List<Product> findAll();

    void deleteById(Long id);

    List<Product> findByDescription(String description);
}
