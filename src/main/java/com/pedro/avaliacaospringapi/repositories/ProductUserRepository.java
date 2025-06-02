package com.pedro.avaliacaospringapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedro.avaliacaospringapi.models.ProductUser;

@Repository
public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {

}
