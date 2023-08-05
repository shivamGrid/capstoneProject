package com.webhook.whatsapp.entity;

import java.util.UUID;

public class ConversationIdGenerator {
    public static String generateConversationId() {
        String uuid = UUID.randomUUID().toString();
        return "c"+uuid.substring(0, 7);
    }
}
