package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.ShoppingCar;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

/**
 * Clase que representa el servicio web de usuarios
 * @author jadir
 *
 */
@RestController
@RequestMapping(value = "users", produces = "application/hal+json")
//@RequestMapping("/api/v1/users")
@Api(tags = "Usuarios")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Para usar @PreAuthorize() annotation: hasRole('ROLE_'), hasAnyRole('ROLE_'), hasAuthority('permission'), hasAnyAuthority('permission')

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    @ApiOperation(value = "Listar usuarios", notes = "Servicio para listar usuarios")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Usuarios listadas correctamente"),
        @ApiResponse(code = 404, message = "Usuarios no encontrados")
    })
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping("/email")
    @PreAuthorize("hasAuthority('users:read')")
    @ApiOperation(value = "Obtener usuario por email", notes = "Servicio para obtener usuario por su email")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Usuario encontrado"),
        @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    public ResponseEntity<User> getUserByEmail(@Valid @Email @RequestParam(value = "email") String email) {
        Optional<User> user = this.userService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
//    @PreAuthorize("hasAuthority('users:write')")
    @ApiOperation(value = "Crear usuario", notes = "Servicio para crear usuarios")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Usuario creado correctamente"),
        @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setId(ObjectId.get().toHexString());
        User newUser = this.userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/sign-up")
    @ApiOperation(value = "Registro de usuario", notes = "Servicio para registro de usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario creado correctamente"),
            @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        user.setId(ObjectId.get().toHexString());
        User newUser = this.userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    @ApiOperation(value = "Actualizar usuario", notes = "Servicio para actualizar usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario actualizado correctamente"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User newUserData) {
        Optional<User> userToUpdate = this.userService.findById(new ObjectId(id));

        if (!userToUpdate.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        userToUpdate.isPresent( () -> {
//            this.userService.update(id, user);
//            return new ResponseEntity<>(HttpStatus.OK);
//        });

//        newData.orElseGet((userToUpdate) -> {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        });

//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

//        this.userService.update(id, newData.get());
        this.userService.update(id, newUserData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    @ApiOperation(value = "Eliminar usuario", notes = "Servicio para eliminar usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario eliminado correctamente"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    public void deleteUser(@PathVariable("id") ObjectId id) {
        Optional<User> userToDelete = this.userService.findById(id);
//        if (userToDelete != null) {
//            this.userService.delete(userToDelete);
//        }

        //        this.userService.delete(userToDelete.get());

        userToDelete.ifPresent(user -> {
            this.userService.delete(userToDelete.get());
        });
    }

//    Carrito de compras
    @GetMapping("/shopping-cars/{id}")
    public ResponseEntity<?> listUserShoppingCars(@PathVariable("id") String userId) {
        List<ShoppingCar> userSHoppingCars = this.userService.getUserShoppingCars(userId);
        return new ResponseEntity<>(userSHoppingCars, HttpStatus.OK);
    }

    // Carritos de compras con paginacion
    @GetMapping("/shopping-cars/{customerId}/pagination")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getUserShoppingCarsPage(@PathVariable("customerId") String customerId,
                                                     @RequestParam Optional<Integer> page,
                                                     @RequestParam Optional<String> sortBy) {
        Page<ShoppingCar> shoppingCarsPage = this.userService.findShoppingCarPageByCustomerId(
                customerId,
                PageRequest.of(
                        page.orElse(0),
                        5,
                        Sort.Direction.ASC,
                        sortBy.orElse("id")
                )
        );
        return new ResponseEntity<>(shoppingCarsPage, HttpStatus.OK);
    }

    @GetMapping("/shopping-cars/{customerId}/{carId}")
    public ResponseEntity<?> findByCar(@PathVariable("customerId") String customerId, @PathVariable("carId") String carId){
        ShoppingCar shoppingCar = userService.findShoppingCarByUser(customerId, carId);
        return new ResponseEntity<>(shoppingCar,HttpStatus.OK);
    }

    @PostMapping("/shopping-cars/{userId}")
    public ResponseEntity<?> updateUserShoppingCar(@RequestBody ShoppingCar shoppingCar, @PathVariable("userId") String userId) {
        List<ShoppingCar> newShoppingCar = this.userService.addProductsToCar(userId, shoppingCar);
        return new ResponseEntity<>(newShoppingCar, HttpStatus.CREATED);
    }

    @PostMapping("/shopping-cars/{customerId}/{idCar}")
    public ResponseEntity<?> deleteProductInCar(@RequestBody ShoppingCar shoppingCars,
                                       @PathVariable("customerId") String customerId, @PathVariable("idCar") String idCar) {
        ShoppingCar newShoppingCar = this.userService.deleteFavoriteProduct(customerId, shoppingCars,idCar);
        return new ResponseEntity<>(newShoppingCar, HttpStatus.OK);
    }
}
