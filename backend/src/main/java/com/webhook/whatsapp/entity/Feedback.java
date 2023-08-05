package com.webhook.whatsapp.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "feedback")
public class Feedback {
    private String userId;
    private String userName;
    private String feedback;
    private String orderAmount;
    private int feedBackCount;

    private String feedBackDate = LocalDate.now().toString();
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public Feedback(String userId, String feedback) {
        this.userId = userId;
        this.feedback = feedback;
    }
}
