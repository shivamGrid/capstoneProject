package com.webhook.whatsapp.repository;

import com.twilio.base.Page;
import com.webhook.whatsapp.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.awt.print.Pageable;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByUserId(String userId);
    Order findFirstByUserIdOrderByLastUpdatedDesc(String userId);
    int countByStatus(String status);
    List<Order> findAllByUserId(String userId);

    Page<Order> findByStatus(String status, Pageable pageable);


}