package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.entity.Cart;
import com.webhook.whatsapp.entity.CartItem;
import com.webhook.whatsapp.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //to add product in user's cart
    @Operation(summary = "This is to add product of a user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Added to user's cart successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to create cart with details you provided",
                    content = @Content)
    })
    @PostMapping("/{userId}")
    public ResponseEntity<Cart> addItemToCart(@PathVariable String userId, @RequestBody CartItem cartItem) {
        return cartService.addToCart(userId,cartItem);
    }

    //to update user's cart
    @Operation(summary = "This is to update product of a user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cart updated successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to update cart with details you provided",
                    content = @Content)
    })
    @PutMapping("/{userId}/update")
    public ResponseEntity<Cart> removeItemToCart(@PathVariable String userId, @RequestBody CartItem cartItem) {
        return cartService.removeFromCart(userId,cartItem);
    }

    //to retrieve cart of a user
    @Operation(summary = "This is to retrieve cart of a user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cart retrieved successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to retrieve cart with details you provided",
                    content = @Content)
    })
    @GetMapping("retrieve-cart/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        return cartService.showCart(userId);
    }

}

