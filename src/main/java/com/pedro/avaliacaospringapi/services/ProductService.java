package com.pedro.avaliacaospringapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pedro.avaliacaospringapi.config.exception.notFound.IdNotFoundException;
import com.pedro.avaliacaospringapi.config.exception.notFound.NameNotFoundException;
import com.pedro.avaliacaospringapi.models.Product;
import com.pedro.avaliacaospringapi.repositories.ProductRepository;


@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IdNotFoundException();
        }
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByDescription(String description) {
        List<Product> products = productRepository.findByDescription(description);
        if (products.isEmpty()) throw new NameNotFoundException();
        return products;

    }

    public Product updateProduct(Long id, Product produtoAtualizado) {
        Product produto = productRepository.findById(id)
                    .orElseThrow(IdNotFoundException::new);

        produto.setDescription(produtoAtualizado.getDescription());
        produto.setPrice(produtoAtualizado.getPrice());
        produto.setCategory(produtoAtualizado.getCategory());

        return productRepository.save(produto);
    }


}
