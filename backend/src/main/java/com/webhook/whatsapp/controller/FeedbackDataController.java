package com.webhook.whatsapp.controller;

import com.webhook.whatsapp.dto.FeedBackCount;
import com.webhook.whatsapp.entity.Feedback;
import com.webhook.whatsapp.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class FeedbackDataController {

    @Autowired
    FeedbackService feedbackService;

    @Operation(summary = "This is to fetch all feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getUniqueUserFeedback();
    }

    @Operation(summary = "This is to receive feedback of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @PostMapping
    public void receiveFeedback(@RequestBody Feedback requestBody) {
        feedbackService.saveFeedback(requestBody);
    }

    @Operation(summary = "This is to count feedback by date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/daily-count")
    public List<FeedBackCount> countFeedbackByDate() {
        return feedbackService.countSimilarDates();
    }

}
