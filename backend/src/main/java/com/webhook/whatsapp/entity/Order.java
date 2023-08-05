package com.webhook.whatsapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String orderId = OrderIdGenerator.generateOrderId();
    private String orderDate;
    private String orderTime;
    private LocalDateTime localDateTime;
    private String userId;
    private List<CartItem> cartItems;
    private double total;
    private OrderStatus status;
    private String shippingAddress;
    private LocalDateTime lastUpdated;



    public Order(String userId, List<CartItem> cartItems, double total,String shippingAddress,OrderStatus status) {
        this.userId = userId;
        this.cartItems = cartItems;
        this.total = total;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }
    public enum OrderStatus{Pending, Processing, Dispatch, Delivered, Cancelled};

}
