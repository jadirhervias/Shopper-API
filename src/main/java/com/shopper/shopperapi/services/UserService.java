package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Método para listar usuarios
     * @return List<User>
     */
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    /**
     * Método para buscar usuario por id
     * @param id
     * @return User
     */
    public User findById(ObjectId id) {
        return this.userRepository.findById(id);
    }

    /**
     * Método para buscar usuario por email
     * @param email
     * @return User
     */
    public User findByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        return user;
    }

    /**
     * Método para crear usuario
     * @param user
     * @return User
     */
    @Transactional
    public User create(User user) {
        user.setId(ObjectId.get());
        return this.userRepository.save(user);
    }

    /**
     * Método para actualizar usuario
     * @param id
     * @param user
     */
    @Transactional
    public void update(ObjectId id, User user) {
        user.setId(id);
        this.userRepository.save(user);
    }

    /**
     * Método para eliminar un usuario
     * @param user
     */
    // Usando directamente el objeto usuario
    @Transactional
    public void delete(User user) {
        this.userRepository.delete(user);
    }
}
