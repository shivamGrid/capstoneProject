package com.webhook.whatsapp.controller;


import com.webhook.whatsapp.entity.Order;
import com.webhook.whatsapp.repository.OrderRepository;
import com.webhook.whatsapp.service.OrderDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderDataController {

@Autowired
    OrderRepository orderRepository;

    private final OrderDataService orderDataService;

    @Operation(summary = "This is to fetch all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Order fetched successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to fetch orders",
                    content = @Content)
    })
    @GetMapping()
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "lastUpdated") String fieldName,
            @RequestParam(defaultValue = "DESC") String sort
    ) {
        Page<Order> orderPage = orderDataService.getOrders(page, size, fieldName, sort, status);
        return new ResponseEntity<>(orderPage, HttpStatus.OK);
    }


    //to get 10 recent orders
    @Operation(summary = "This is to fetch recent 10 orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/recent")
    public List<Order> getOrders() {
        return orderDataService.getRecentOrders();
    }

}
