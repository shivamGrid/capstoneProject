package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.entity.Admin;
import com.webhook.whatsapp.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping()
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;

    @Operation(summary = "This is for checking user login credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Login successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to login with details you provided",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Admin loginRequest) {
        return adminService.checkDetails(loginRequest);
    }

}
