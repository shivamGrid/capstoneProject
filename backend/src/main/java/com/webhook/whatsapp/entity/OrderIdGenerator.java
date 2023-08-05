package com.webhook.whatsapp.entity;

import java.util.UUID;

public class OrderIdGenerator {
    public static String generateOrderId() {
        String uuid = UUID.randomUUID().toString();
        return "#"+uuid.substring(0, 6);
    }
    }