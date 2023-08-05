package com.webhook.whatsapp.controller;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.webhook.whatsapp.entity.Conversation;
import com.webhook.whatsapp.repository.ConversationRepository;
import com.webhook.whatsapp.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.webhook.whatsapp.util.Constant.*;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConversationDataController {

    private final ConversationService conversationService;
    @Autowired
    ConversationRepository conversationRepository;

    //to fetch all the conversation
    @Operation(summary = "This is to fetch all the conversation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Conversation fetched successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to fetch conversation with details you provided",
                    content = @Content)
    })
    @GetMapping()
    public ResponseEntity<Page<Conversation>> getAllConversation(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "25") int size,
                                                                 @RequestParam(defaultValue = "lastUpdated") String fieldName,
                                                                 @RequestParam(defaultValue = "DESC") String sort) {
        Page<Conversation> conversationPage = conversationService.getConversations(page, size, fieldName, sort);
        return new ResponseEntity<>(conversationPage, HttpStatus.OK);
    }

    @Operation(summary = "This is to count total users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Total users fetched successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to fetch users",
                    content = @Content)
    })
    @GetMapping("/users")
    public double countUsers(){
        try (MongoClient mongoClient = MongoClients.create(MONGO_CLIENT)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(CONVERSATIONS_COLLECTION);
            return collection.countDocuments();
        }
    }

}
