package com.webhook.whatsapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class CurrentDateController {

    @Operation(summary = "This is to get today's date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/date")
    public String currentDate() {
        LocalDate currentDate = LocalDate.now();

        // Create a formatter with the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");

        // Format the current date using the formatter

        return currentDate.format(formatter);

    }

}
