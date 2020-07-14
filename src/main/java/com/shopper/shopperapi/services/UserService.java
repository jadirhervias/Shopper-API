package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.ShoppingCar;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
     * Método para listar por rol
     * @return List<User>
     */
    public List<User> findByRole(String role) {
        return this.userRepository.findByRole(role);
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
        return this.userRepository.findByEmail(email);
    }

    /**
     * Método para obtener el nombre del usuario
     * @param id
     * @return String
     */
    public String getUserFirstName(String id) {
        return userRepository.findById(id).get().getFirstName();
    }

    /**
     * Método para buscar notification key por ID del usuario
     * @param id
     * @return String
     */
    public String getUserNotificationKey(String id) {
        User user = userRepository.findById(id).get();
        assert user.getNotificationDeviceGroup() != null;
        return user.getNotificationDeviceGroup().get("notification_key");
    }

    /**
     * Método para actualizar notification key por ID del usuario
     * @param userId-
     * @param notificationKeyName-
     * @param newNotificationKey-
     */
    public void setUserNotificationKey(String userId, String notificationKeyName, String newNotificationKey) {

        User user = this.findById(new ObjectId(userId)).get();
        Map<String, String> newUserNotificationDeviceGroup = new HashMap<>();

        newUserNotificationDeviceGroup.put("notification_key_name", notificationKeyName);
        newUserNotificationDeviceGroup.put("notification_key", newNotificationKey);

        user.setNotificationDeviceGroup(newUserNotificationDeviceGroup);
    }

    /**
     * Método para buscar notification key name por ID del usuario
     * @param id
     * @return String
     */
    public String getUserNotificationKeyName(String id) {
        User user = userRepository.findById(id).get();
        assert user.getNotificationDeviceGroup() != null;
        return user.getNotificationDeviceGroup().get("notification_key_name");
    }

    /**
     * Método para crear usuario
     * @param user
     * @return User
     */
    @Transactional
    public User create(User user) {
        user.setId(ObjectId.get().toHexString());
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
        user.setId(id.toHexString());
        this.userRepository.save(user);
    }

    /**
     * Método para eliminar un usuario usando directamente el objeto usuario
     * @param user
     */
    @Transactional
    public void delete(User user) {
        this.userRepository.delete(user);
    }

    // Carrito de compras del usuario
    public List<ShoppingCar> userShoppingCars(String id_user){
        Optional<User> user = userRepository.findById(id_user);
        return user.get().getShoppingCars();
    }

    public List<ShoppingCar> addProducts(String id_user, ShoppingCar shoppingCars) {
        Optional<User> user = userRepository.findById(id_user);
        List<ShoppingCar> favorite = new ArrayList<>();
        if (user.get().getShoppingCars() != null) {
            if (shoppingCars.getId() != null) {
                favorite = user.get().getShoppingCars();
                for (int i = 0; i < favorite.size(); i++) {
                    ShoppingCar car = favorite.get(i);
                    if (car.getId().equals(shoppingCars.getId())) {
                        List<Product> products = car.getProducts();
                        for (Product pro : shoppingCars.getProducts()) {
                            products.add(pro);
                        }
                        favorite.get(i).setProducts(products);
                        user.get().setShoppingCars(favorite);
                        userRepository.save(user.get());
                        break;
                    }
                }
            }else {
                favorite = user.get().getShoppingCars();
                shoppingCars.setId(ObjectId.get().toHexString());
                favorite.add(shoppingCars);
                user.get().setShoppingCars(favorite);
                userRepository.save(user.get());
            }
        }else {
            shoppingCars.setId(ObjectId.get().toHexString());
            favorite.add(shoppingCars);
            user.get().setShoppingCars(favorite);
            userRepository.save(user.get());
        }

        return user.get().getShoppingCars();
    }
}
