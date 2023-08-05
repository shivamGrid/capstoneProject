package com.webhook.whatsapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "product")
public class Product {
    @Id
    private String productId;
    private int index;
    private String product; //product name
    private String category;
    private String sub_category;
    private String brand;
    private double sale_price;
    private double market_price;
    private String type;
    private double rating;
    private String description;
    private String url;
}