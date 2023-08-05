package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.entity.ParseJSON;
import com.webhook.whatsapp.service.ReceiveMessageService;
import com.webhook.whatsapp.service.SendMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class WhatsAppController {

    private final ReceiveMessageService receiveMessageService;
    private final SendMessageService sendMessageService;

    @Operation(summary = "This is to receive message from whatsApp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Message received successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to receive message",
                    content = @Content)
    })
    @PostMapping("/webhook2")
    public ResponseEntity<String> webhookHandler(@RequestBody String requestBody) {
       return receiveMessageService.receiveMessage(requestBody);
    }

    @Operation(summary = "This is to send whatsApp message to the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @PostMapping("/send-message")
    public void sendBack(@RequestBody ParseJSON requestBody){
        sendMessageService.sendMessage(requestBody);
    }


}