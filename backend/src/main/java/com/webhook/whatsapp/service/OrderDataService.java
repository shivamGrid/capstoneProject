package com.webhook.whatsapp.service;

import com.webhook.whatsapp.entity.Order;
import com.webhook.whatsapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrderDataService {

    @Autowired
    OrderRepository orderRepository;


    public Page<Order> getOrders(int page, int size, String fieldName, String sortOrder, String status) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(direction, fieldName);
        Pageable pageable = PageRequest.of(page, size, sort);

            return orderRepository.findAll(pageable);
    }

    public List<Order> getRecentOrders() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "lastUpdated"));
        return orderRepository.findAll(pageRequest).getContent();
    }


}
