package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.service.CountDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/count")
@CrossOrigin(origins = "*")
public class CountDataController {
    public final CountDataService countDataService;

    //to count all the orders received
    @Operation(summary = "This is to count all the orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/orders")
    public String countOrders() {
        return countDataService.allOrders();
    }


    //to count total users
    @Operation(summary = "This is to count all the users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/users")
    public double countUsers() {
        return countDataService.allUsers();
    }


   //to count total sales amount of all orders
   @Operation(summary = "This is to count total amount of all orders")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200",
                   description = "Success",
                   content = {@Content(mediaType = "application/json")}),
           @ApiResponse(responseCode = "400",
                   description = "Failed",
                   content = @Content)
   })
    @GetMapping("/sales/amount")
    public double countSales(){
        return countDataService.totalSalesAmount();
    }

    //to count total pending orders
    @Operation(summary = "This is to count all pending orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/pending")
    public int countPendingOrders() {
        return countDataService.allPendingOrders();
    }

    //to count total processing orders
    @Operation(summary = "This is to count total processing orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/processing")
    public int countProcessingOrders() {
        return countDataService.allProcessingOrders();
    }

    //to count total dispatch orders
    @Operation(summary = "This is to count total dispatch orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/dispatch")
    public int countDispatchOrders() {
        return countDataService.allDispatchOrders();
    }

    //to count total delivered orders
    @Operation(summary = "This is to count total delivered orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/delivered")
    public int countDeliveredOrders() {
        return countDataService.allDeliveredOrders();
    }

    //to count total cancelled orders
    @Operation(summary = "This is to count total cancelled orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/canceled")
    public int countCancelledOrders() {
        return countDataService.allCancelledOrders();
    }

    //to count total feedback
    @Operation(summary = "This is to count total feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/feedback")
    public String countTotalFeedback() {
        return countDataService.allFeedback();
    }


}
