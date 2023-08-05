package com.webhook.whatsapp.service;

import com.webhook.whatsapp.repository.CartRepository;
import com.webhook.whatsapp.repository.ProductRepository;
import com.webhook.whatsapp.entity.Cart;
import com.webhook.whatsapp.entity.CartItem;
import com.webhook.whatsapp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;

    public ResponseEntity<Cart>addToCart(String userId,CartItem cartItem) {
        //Retrieve the user's cart
        Cart cart = cartRepository.findByUserId(userId);
        // If the cart doesn't exist, create
        if (cart == null) {
            cart = new Cart(userId, new ArrayList<>());
        }
        //Retrieve the product from the database
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            // If the product doesn't exist then 404 error
            if (product == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //Add the item to the cart
            cart.addCartItem(cartItem);
        //Calculate the new total
        double total = 0;
        for (CartItem item : cart.getCartItems()) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            if (p != null) {
                total += p.getSale_price() * item.getQuantity();
            }
        }
        cart.setTotal(total);

        //Save the updated cart
        cartRepository.save(cart);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    public ResponseEntity<Cart>removeFromCart(String userId, CartItem cartItem) {
        //Retrieve the user's cart
        Cart cart = cartRepository.findByUserId(userId);
        // If the cart doesn't exist, create
        if (cart == null) {
            cart = new Cart(userId, new ArrayList<>());
        }
        //Retrieve the product from the database
        Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
        // If the product doesn't exist then 404 error
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //Remove the item to the cart
        cart.removeCartItem(cartItem);
        cart.getCartItems().removeIf(item -> item.getQuantity() == 0);
        //Calculate the new total
        double total = 0;
        for (CartItem item : cart.getCartItems()) {
            Product p = productRepository.findById(item.getProductId()).orElse(null);
            if (p != null) {
                total += p.getSale_price() * item.getQuantity();
            }
        }
        cart.setTotal(total);

        //Save the updated cart
        cartRepository.save(cart);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    public ResponseEntity<Cart>showCart(String userId) {
        // Retrieve the user's cart
        Cart cart = cartRepository.findByUserId(userId);

        // If the cart doesn't exist, return a 404 error
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : cartItems) {
            String productId = item.getProductId();
            Optional<Product> optionalProduct = productRepository.findById(productId);

            // Get the product from Optional or default to null
            Product product = optionalProduct.orElse(null);
            // Update the cart item with the product name
            if (product != null) {
                item.setProductId(product.getProduct());
            }
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }




}
