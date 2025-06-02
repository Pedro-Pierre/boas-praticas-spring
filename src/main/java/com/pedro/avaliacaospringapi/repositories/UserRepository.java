package com.pedro.avaliacaospringapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pedro.avaliacaospringapi.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

    @Query(value = "SELECT * FROM TB_USERS", nativeQuery = true)
    List<User> findAll();

    void deleteById(Long id);

    List<User> findByName(String name);

}
