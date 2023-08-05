package com.webhook.whatsapp.service;

import com.mongodb.client.*;
import com.webhook.whatsapp.repository.OrderRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import static com.webhook.whatsapp.util.Constant.*;

@Service
public class CountDataService {

    @Autowired
    OrderRepository orderRepository;

    public String allOrders() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_CLIENT)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(ORDER_COLLECTION);
            return String.valueOf(collection.countDocuments());
        }
    }

    public double allUsers() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_CLIENT)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(CONVERSATIONS_COLLECTION);
            return collection.countDocuments();
        }
    }

    public double totalSalesAmount() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_CLIENT)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(ORDER_COLLECTION);
            MongoCursor<Document> cursor = collection.find().iterator();
            double totalPrice = 0.0;
            while(cursor.hasNext()) {
                Document document = cursor.next();
                double documentTotalPrice = document.getDouble(ORDER_TOTAL_FIELD);
                totalPrice += documentTotalPrice;
            }
            DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_PATTERN);
            return Double.parseDouble(decimalFormat.format(totalPrice));
        }
    }

    public int allPendingOrders() {
        return orderRepository.countByStatus(PENDING_STATUS);
    }

    public int allDispatchOrders() {
        return orderRepository.countByStatus(DISPATCH_STATUS);
    }

    public int allDeliveredOrders() {
        return orderRepository.countByStatus(DELIVERED_STATUS);
    }

    public int allProcessingOrders() {
        return orderRepository.countByStatus(PROCESSING_STATUS);
    }

    public int allCancelledOrders() {
        return orderRepository.countByStatus(CANCELLED_STATUS);
    }

    public String allFeedback() {
        try (MongoClient mongoClient = MongoClients.create(MONGO_CLIENT)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(FEEDBACK_COLLECTION);
            return String.valueOf(collection.countDocuments());
        }
    }

}
