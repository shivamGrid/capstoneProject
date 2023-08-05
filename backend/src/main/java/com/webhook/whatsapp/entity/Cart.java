package com.webhook.whatsapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "cart")
public class Cart {
    @Id
    private String cartId;
    private String userId;
    private List<CartItem> cartItems;
    private double total;
    private LocalDateTime lastUpdated;

    public Cart(String userId, List<CartItem> cartItems) {
        this.userId = userId;
        this.cartItems = cartItems;
        this.total = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    public void addCartItem(CartItem cartItem) {
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(cartItem);
        }
    }

    public void removeCartItem(CartItem cartItem) {
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(cartItem.getProductId())) {
                found = true;
                if(item.getQuantity()>=cartItem.getQuantity()){
                item.setQuantity(item.getQuantity() - cartItem.getQuantity());
                break;}
            }
        }
        if (!found) {
            cartItems.add(cartItem);
        }
    }

    public void clearCart() {
        cartItems.clear();
        total = 0;
    }
}
