package com.pedro.avaliacaospringapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pedro.avaliacaospringapi.config.exception.notFound.IdNotFoundException;
import com.pedro.avaliacaospringapi.config.exception.notFound.NameNotFoundException;
import com.pedro.avaliacaospringapi.models.User;
import com.pedro.avaliacaospringapi.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public List<User> getUsersByName(String name)  {
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

}
