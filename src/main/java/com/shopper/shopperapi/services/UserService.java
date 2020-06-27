package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public Optional<User> findById(ObjectId id) {
        return this.userRepository.findById(id);
    }

    /**
     * Método para buscar usuario por email
     * @param email
     * @return User
     */
    public Optional<User> findByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    
    public String getUserNotificationKeyName(String id) {
        User user = userRepository.findById(id).get();
//        return user.getUserNotificationKeyName();
        assert user.getNotificationDeviceGroup() != null;
        return user.getNotificationDeviceGroup().get("notification_key_name");
    }
    
    public String getUserNotificationKey(String id) {
        User user = userRepository.findById(id).get();
//        return user.getUserNotificationKey();
        assert user.getNotificationDeviceGroup() != null;
        return user.getNotificationDeviceGroup().get("notification_key");
    }
    
    public String getUserFirstName(String id) {
        User user = userRepository.findById(id).get();
        return user.getFirstName();
    }
}
