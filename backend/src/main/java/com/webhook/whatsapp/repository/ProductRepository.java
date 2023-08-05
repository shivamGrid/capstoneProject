package com.webhook.whatsapp.repository;


import com.webhook.whatsapp.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ 'productId' : ?0 }")
    Optional<Product> findByProductId(String productId);
}