package com.shopper.shopperapi.resources.controller;


import java.util.Comparator;
import java.util.List;

import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopper.shopperapi.models.ResponseShopsOrder;
import com.shopper.shopperapi.models.User;
import com.shopper.shopperapi.services.ShopService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/orders",produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.COLLECTION_JSON_VALUE })
public class OrderController {
	private final ShopService shopService;
	
	public OrderController(ShopService shopService) {
        this.shopService = shopService;
    }
	
	@PostMapping
	@RestResource
    @ApiOperation(value = "Lista catalogos", notes = "Lista de catalogos por distancia")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Listado correcto"),
            @ApiResponse(code = 404, message = "Problemas en el listado")
    })
    public List<ResponseShopsOrder> shop_order(@RequestBody User user){
		List<ResponseShopsOrder> orderShop = this.shopService.OrdenarTiendas(user.getUser_lat(),user.getUser_lng(),user.idShop());
		orderShop.sort(Comparator.comparing(ResponseShopsOrder::getDistancia));
		return orderShop;
    }
}
