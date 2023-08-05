package com.webhook.whatsapp.service;

import com.webhook.whatsapp.repository.AdminRepository;
import com.webhook.whatsapp.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;

    public ResponseEntity<String> checkDetails(Admin loginRequest){
        Admin user = adminRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.ok(user.getUsername());
    }
}
