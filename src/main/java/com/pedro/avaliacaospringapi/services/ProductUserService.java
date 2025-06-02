package com.pedro.avaliacaospringapi.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.pedro.avaliacaospringapi.config.exception.notFound.IdNotFoundException;
import com.pedro.avaliacaospringapi.dtos.ProductUserDto;
import com.pedro.avaliacaospringapi.models.Product;
import com.pedro.avaliacaospringapi.models.ProductUser;
import com.pedro.avaliacaospringapi.models.User;
import com.pedro.avaliacaospringapi.repositories.ProductUserRepository;

@Service
public class ProductUserService {

    private ProductUserRepository productUserRepository;

    public ProductUserService(ProductUserRepository productUserRepository) {
        this.productUserRepository = productUserRepository;
    }

    public List<ProductUser> getAllProductsUsers() {
        return productUserRepository.findAll();
    }

    public ProductUser addProductUser(ProductUserDto dto) {
        try {
            User user = new User();
            user.setId(dto.userId());

            Product product = new Product();
            product.setId(dto.productId());

            ProductUser productUser = new ProductUser();
            productUser.setUser(user);
            productUser.setProduct(product);

            return productUserRepository.save(productUser);

        } catch (DataIntegrityViolationException e) {
            throw new IdNotFoundException();
        }
    }


    public void deleteProductUserById(Long id) {
        productUserRepository.deleteById(id);
    }

    public ProductUser updateProductUser(Long id, ProductUserDto dto) {
        ProductUser productUser = productUserRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);

        try {
            User user = new User();
            user.setId(dto.userId());

            Product product = new Product();
            product.setId(dto.productId());

            productUser.setUser(user);
            productUser.setProduct(product);

            return productUserRepository.save(productUser);

        } catch (DataIntegrityViolationException e) {
            throw new IdNotFoundException();
        }
    }

    }
