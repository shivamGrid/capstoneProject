package com.webhook.whatsapp.repository;

import com.webhook.whatsapp.dto.FeedBackCount;
import com.webhook.whatsapp.entity.Feedback;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedbackRepository extends MongoRepository<Feedback,String> {
    @Aggregation(pipeline = {
            "{$group: { _id: { $dateToString: { format: '%Y-%m-%d', date: '$lastUpdated' } }, count: { $sum: 1 } }}",
            "{$sort: { _id: -1 }}",
            "{$limit: 5}"
    })
    List<FeedBackCount> countSimilarDates();
    List<Feedback> findAllByUserId(String userId);

}
