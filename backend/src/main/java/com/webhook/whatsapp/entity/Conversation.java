package com.webhook.whatsapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "conversations")
public class Conversation {
    @Id
    private String conversationId = ConversationIdGenerator.generateConversationId();
    private String phoneNumber;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private List<ConversationList> messages;

    private LocalDateTime lastUpdated;

    public Conversation() {
        this.messages = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

}
