package com.webhook.whatsapp.repository;

import com.webhook.whatsapp.dto.VisitorCountDto;
import com.webhook.whatsapp.entity.Conversation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Conversation findByPhoneNumber(String phoneNumber);
    @Aggregation(pipeline = {
            "{$group: { _id: '$startDate', count: { $sum: 1 } }}",
            "{$sort: { _id: -1 }}",
            "{$limit: 7}"

    })
    List<VisitorCountDto> countSimilarDates();
}