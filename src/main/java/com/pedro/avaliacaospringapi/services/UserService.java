package com.pedro.avaliacaospringapi.services;

import com.pedro.avaliacaospringapi.config.exception.notFound.IdNotFoundException;
import com.pedro.avaliacaospringapi.config.exception.notFound.NameNotFoundException;
import com.pedro.avaliacaospringapi.models.Product;
import com.pedro.avaliacaospringapi.models.User;
import com.pedro.avaliacaospringapi.repositories.ProductRepository;
import com.pedro.avaliacaospringapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UserService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // --- Métodos básicos de usuário ---

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IdNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public List<User> getUsersByName(String name) {
        List<User> users = userRepository.findByName(name);
        if (users.isEmpty()) throw new NameNotFoundException();
        return users;
    }

    public User updateUser(Long id, User usuarioAtualizado) {
        User usuario = userRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);

        usuario.setName(usuarioAtualizado.getName());
        usuario.setEmail(usuarioAtualizado.getEmail());

        return userRepository.save(usuario);
    }

    // --- Métodos para relação com produtos ---

    public Set<Product> listarProdutosDoUsuario(Long userId) {
        User user = buscarUsuario(userId);
        return user.getProducts();
    }

    public void adicionarProdutoAoUsuario(Long userId, Long productId) {
        User user = buscarUsuario(userId);
        Product product = buscarProduto(productId);
        user.getProducts().add(product);
        userRepository.save(user);
    }

    public void removerProdutoDoUsuario(Long userId, Long productId) {
        User user = buscarUsuario(userId);
        Product product = buscarProduto(productId);
        user.getProducts().remove(product);
        userRepository.save(user);
    }

    public void atualizarProdutosDoUsuario(Long userId, List<Long> idsProdutos) {
        User user = buscarUsuario(userId);
        List<Product> produtos = productRepository.findAllById(idsProdutos);
        user.setProducts(new HashSet<>(produtos));
        userRepository.save(user);
    }

    private User buscarUsuario(Long id) {
        return userRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);
    }

    private Product buscarProduto(Long id) {
        return productRepository.findById(id)
                .orElseThrow(IdNotFoundException::new);
    }
}
