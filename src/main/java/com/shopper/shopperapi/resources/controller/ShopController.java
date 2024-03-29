package com.shopper.shopperapi.resources.controller;

import com.shopper.shopperapi.models.Coordenates;
import com.shopper.shopperapi.models.ResponseShopsOrder;
import com.shopper.shopperapi.models.Shop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.shopper.shopperapi.services.ShopService;
import org.bson.types.ObjectId;
//import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.annotation.RestResource;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
//import java.awt.print.Pageable;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

@RestController
//@Controller
//JSON sin HAL
//@RequestMapping(value = "api/v1/shops", produces = "application/hal+json")
@RequestMapping(value = "shops", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@Api(tags = "Tiendas")
@CrossOrigin(origins = "*")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    @RestResource()
    @ApiOperation(value = "Listar catálogos", notes = "Servicio para listar catálogos")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Catálogo listados correctamente"),
        @ApiResponse(code = 404, message = "Catálogos no encontrados")
    })
    public ResponseEntity<?> listShops(
//                @PageableDefault(size = 25) Pageable pageRequest,
//                PagedResourcesAssembler<Shop> pagedResourcesAssembler
                ) throws URISyntaxException {

//        Page<Shop> films = this.dvdRentalService.retrieveFilms(pageRequest);
//        Shop selfLink = new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
//        PagedResources<ShopService> result = pagedResourcesAssembler.toResource(films, this.filmResourceAssembler, selfLink);
//        return new ResponseEntity<>(result, HttpStatus.OK);

        List<Shop> shops = shopService.findAll();
//        CollectionModel<Shop> resources = new CollectionModel<>(shops);

//        return ResponseEntity.ok(resources);
        return ResponseEntity.ok(shops);
    }

    /**
     * Obtener las tiendas ordenadas según la distancia a la que se encuentran del usuario
     */
    @GetMapping("/sorted")
    public ResponseEntity<?> getNearestShopsForUser(@RequestBody @Valid Coordenates coordenates) {

        double userLat = coordenates.getLatitude();
        double userLng = coordenates.getLongitude();

        List<ResponseShopsOrder> nearestShops = shopService.getNearestShopsForUser(userLat, userLng, null);
        nearestShops.sort(Comparator.comparing(ResponseShopsOrder::getDistance));

        return ResponseEntity.ok(nearestShops);
    }

    /**
     * Obtener la distancia entre la tienda y el usuario
     */
    @GetMapping("/distance/{shopId}")
    public ResponseEntity<?> getShopDistanceForUser(
            @RequestBody @Valid Coordenates coordenates,
            @PathVariable("shopId") @Nullable String shopId) {

        double userLat = coordenates.getLatitude();
        double userLng = coordenates.getLongitude();

        List<ResponseShopsOrder> shopDistance = shopService.getNearestShopsForUser(userLat, userLng, shopId);
        return ResponseEntity.ok(shopDistance);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener catálogo por ID", notes = "Servicio para obtener catálogo por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Catálogo encontrado"),
            @ApiResponse(code = 404, message = "Catálogo no encontrado")
    })
    public ResponseEntity<Shop> getShopById(@PathVariable("id") ObjectId id) {
        Shop shop = this.shopService.findById(id);
        if (shop == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(shop);
    }

    @PostMapping
    @ApiOperation(value = "Crear catálogo", notes = "Servicio para crear catálogos")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Catálogo creado correctamente"),
        @ApiResponse(code = 400, message = "Solicitud Inválida")
    })
    public ResponseEntity<Shop> createShop(@Valid @RequestBody Shop shop) {
        shop.setId(ObjectId.get().toHexString());
        Shop newShop = this.shopService.create(shop);
        return new ResponseEntity<>(newShop, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar catálogo", notes = "Servicio para actualizar un catálogo")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Catálogo actualizado correctamente"),
        @ApiResponse(code = 404, message = "Catálogo no encontrado")
    })
    public ResponseEntity<Shop> updateShop(@PathVariable("id") ObjectId id, @Valid @RequestBody Shop data) {
        Shop shopToUpdate = this.shopService.findById(id);
        if (shopToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.shopService.update(id, data);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar catálogo", notes = "Servicio para eliminar un catálogo")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Catálogo eliminado correctamente"),
        @ApiResponse(code = 404, message = "Catálogo no encontrado")
    })
    public void deleteShop(@PathVariable("id") ObjectId id) {
        Shop shopToDelete = this.shopService.findById(id);
        if (shopToDelete != null) {
            this.shopService.delete(shopToDelete);
        }
    }
}
