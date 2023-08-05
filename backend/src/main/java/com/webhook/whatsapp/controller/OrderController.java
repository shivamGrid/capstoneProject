package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.entity.Order;
import com.webhook.whatsapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    OrderService orderService;

    //to create an order
    @Operation(summary = "This is to create order of a user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Order created successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to create order with details you provided",
                    content = @Content)
    })
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable String userId, @RequestBody String shippingAddress) {
        return orderService.createOrder(userId, shippingAddress);
    }

    //to retrieve order of a user
    @Operation(summary = "This is to retrieve order of a user by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Order created successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to create order with details you provided",
                    content = @Content)
    })
    @GetMapping("retrieve-order/{userId}")
    public ResponseEntity<Order> getOrder(@PathVariable String userId) {
        return orderService.showOrder(userId);
    }

      //to cancel order of a user
//    @PutMapping("/{userId}/cancel")
//    public ResponseEntity<String> cancelOrder(@PathVariable String userId) {
//       return orderService.cancelAnOrder(userId);
//   }

}
