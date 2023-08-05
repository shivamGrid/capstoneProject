package com.webhook.whatsapp.repository;


import com.twilio.base.Page;
import com.webhook.whatsapp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;

public interface UserRepository extends MongoRepository<User, String> {
}
