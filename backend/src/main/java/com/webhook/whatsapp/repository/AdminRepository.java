package com.webhook.whatsapp.repository;

import com.webhook.whatsapp.entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByEmail(String email);
}
