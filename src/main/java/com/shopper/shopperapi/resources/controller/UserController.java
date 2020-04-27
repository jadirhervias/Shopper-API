package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.services.UserService;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import io.swagger.annotations.Api;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

/**
 * Clase que representa el servicio web de usuarios
 * @author jadir
 *
 */
@RestController
@RequestMapping("/api/v1/users")
// @Api(tags = "usuario")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
//    @ApiOperation(value = "Listar usuarios", notes = "Servicio para listar usuarios")
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = "Usuarios listadas correctamente"),
//        @ApiResponse(code = 404, message = "Usuarios no encontrados")
//    })
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping("/email")
//    @ApiOperation(value = "Obtener usuario por email", notes = "Servicio para obtener usuario por su email")
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = "Usuario encontrado"),
//        @ApiResponse(code = 404, message = "Usuario no encontrado")
//    })
    public ResponseEntity<User> getUserByEmail(@Valid @Email @RequestParam(value = "email") String email) {
        User user = this.userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping
//    @ApiOperation(value = "Crear usuario", notes = "Servicio para crear usuarios")
//    @ApiResponses(value = {
//        @ApiResponse(code = 201, message = "Usuario creado correctamente"),
//        @ApiResponse(code = 400, message = "Solicitud Inválida")
//    })
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setId(ObjectId.get());
        User newUser = this.userService.create(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
//    @ApiOperation(value = "Actualizar usuario", notes = "Servicio para actualizar usuarios")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Usuario actualizado correctamente"),
//            @ApiResponse(code = 404, message = "Usuario no encontrado")
//    })
    public ResponseEntity<User> updateUser(@PathVariable("id") ObjectId id, @Valid @RequestBody User user) {
        User newData = this.userService.findById(id);
        if (newData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.userService.update(id, newData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @ApiOperation(value = "Eliminar usuario", notes = "Servicio para eliminar usuarios")
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Usuario eliminado correctamente"),
//            @ApiResponse(code = 404, message = "Usuario no encontrado")
//    })
    public void deleteUser(@PathVariable("id") ObjectId id) {
        User userToDelete = this.userService.findById(id);
        if (userToDelete != null) {
            this.userService.delete(userToDelete);
        }
    }
}
