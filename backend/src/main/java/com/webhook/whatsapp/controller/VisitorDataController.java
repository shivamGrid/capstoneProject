package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.dto.VisitorCountDto;
import com.webhook.whatsapp.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/visitor")
@CrossOrigin(origins = "*")
public class VisitorDataController {
   @Autowired
    ConversationService conversationService;

    @Operation(summary = "This is to count daily new users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/daily-count")
    public List<VisitorCountDto> countVisitorByDate() {
        return conversationService.countSimilarDates();
    }

}
