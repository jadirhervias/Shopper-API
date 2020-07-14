package com.shopper.shopperapi.services;

import com.shopper.shopperapi.models.Product;
import com.shopper.shopperapi.models.ShoppingCar;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
     * @param newUserData
     */
    @Transactional
    public void update(String id, User newUserData) {
        User user = userRepository.findById(new ObjectId(id)).get();
        user.setId(id);
        user.setAddress(newUserData.getAddress());
        user.setFirstName(newUserData.getFirstName());
        user.setLastName(newUserData.getLastName());
        user.setPhoneNumber(newUserData.getPhoneNumber());
        user.setUserLat(newUserData.getUserLat());
        user.setUserLng(newUserData.getUserLng());
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

    public double calculateTotalCost(List<Product> products) {
        double totalCost = products.stream().mapToDouble((product) -> product.getCost() * product.getQuantity()).sum();
        return Math.round(totalCost * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public int calculateQuantity(List<Product> products) {
        return products.stream().mapToInt(Product::getQuantity).sum();
    }

    public List<ShoppingCar> addProducts(String id_user, ShoppingCar shoppingCar) {
        Optional<User> user = userRepository.findById(id_user);
        List<ShoppingCar> userShoppingCars = new ArrayList<>();
        if (user.get().getShoppingCars() != null) {
            if (shoppingCar.getId() != null) {
                userShoppingCars = user.get().getShoppingCars();
                for (int i = 0; i < userShoppingCars.size(); i++) {
                    ShoppingCar car = userShoppingCars.get(i);
                    if (car.getId().equals(shoppingCar.getId())) {
                        List<Product> products = car.getProducts();
                        for (Product pro : shoppingCar.getProducts()) {
                            products.add(pro);
                        }
                        car.setProducts(products);

                        int count = this.calculateQuantity(car.getProducts());
                        car.setCount(count);

                        double totalCost = this.calculateTotalCost(car.getProducts());
                        car.setTotalCost(totalCost);

                        user.get().setShoppingCars(userShoppingCars);
                        userRepository.save(user.get());
                        break;
                    }
                }
            } else {
                /**
                 * TODO: Remover porque es redundante y puede ser que nunca se ejecute
                 */
                userShoppingCars = user.get().getShoppingCars();
                shoppingCar.setId(ObjectId.get().toHexString());
                userShoppingCars.add(shoppingCar);
                user.get().setShoppingCars(userShoppingCars);
                userRepository.save(user.get());
            }
        } else {
            shoppingCar.setId(ObjectId.get().toHexString());

            int count = this.calculateQuantity(shoppingCar.getProducts());
            shoppingCar.setCount(count);

            double totalCost = this.calculateTotalCost(shoppingCar.getProducts());
            shoppingCar.setTotalCost(totalCost);

            // agregar obj Shopping Car
            userShoppingCars.add(shoppingCar);
            // agregar lista de Shopping cars
            user.get().setShoppingCars(userShoppingCars);
            userRepository.save(user.get());
        }

        return user.get().getShoppingCars();
    }
}
