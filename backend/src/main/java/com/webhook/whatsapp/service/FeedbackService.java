package com.webhook.whatsapp.service;

import com.webhook.whatsapp.repository.FeedbackRepository;
import com.webhook.whatsapp.repository.OrderRepository;
import com.webhook.whatsapp.repository.UserRepository;
import com.webhook.whatsapp.dto.FeedBackCount;
import com.webhook.whatsapp.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    public FeedbackService(UserRepository userRepository, FeedbackRepository feedbackRepository,OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.orderRepository = orderRepository;
    }

   public List<Feedback> getUniqueUserFeedback() {
    Set<String> uniqueUserIds = new HashSet<>();
    List<Feedback> uniqueUserFeedbackList = new ArrayList<>();
    List<Feedback> feedbackList = feedbackRepository.findAll(Sort.by(Sort.Direction.DESC, "lastUpdated"));

     for (Feedback feedback : feedbackList) {
        String userId = feedback.getUserId();
        if (!uniqueUserIds.contains(userId)) {
            uniqueUserIds.add(userId);
            uniqueUserFeedbackList.add(feedback);
        }
     }
    return uniqueUserFeedbackList;
   }

    public void saveFeedback(Feedback feedback) {
        Optional<User> userOptional = userRepository.findById(feedback.getUserId());
        String userId = feedback.getUserId();
        List<Order> userOrders = orderRepository.findAllByUserId(userId);
        List<Feedback> userFeedBack = feedbackRepository.findAllByUserId(userId);
        feedback.setOrderAmount("0.0");

        if (userOptional.isPresent()) {
            User user = userOptional.get();
        String fullName = user.getUsername();
         if (!userOrders.isEmpty()) {
            double totalAmount = 0.0;
            // Calculate the total order amount
            for (Order order : userOrders) {
                totalAmount += order.getTotal();
            }
            feedback.setOrderAmount(String.valueOf(totalAmount));
         }
         if(!userFeedBack.isEmpty()){
             feedback.setFeedBackCount(userFeedBack.size()+1);
         } else {
             feedback.setFeedBackCount(1);
         }
        feedback.setUserName(fullName);
        }
        feedback.setUserId(userId);
        feedback.setFeedback(feedback.getFeedback());
        feedbackRepository.save(feedback);
    }

    public List<FeedBackCount> countSimilarDates() {
        return feedbackRepository.countSimilarDates();
    }
    }
