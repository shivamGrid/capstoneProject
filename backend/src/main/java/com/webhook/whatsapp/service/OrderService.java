package com.webhook.whatsapp.service;

import com.webhook.whatsapp.repository.CartRepository;
import com.webhook.whatsapp.repository.OrderRepository;
import com.webhook.whatsapp.repository.ProductRepository;
import com.webhook.whatsapp.entity.Cart;
import com.webhook.whatsapp.entity.CartItem;
import com.webhook.whatsapp.entity.Order;
import com.webhook.whatsapp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;


  public ResponseEntity<String> createOrder(String userId, String shippingAddress) {
      // Retrieve the user's cart
      Cart cart = cartRepository.findByUserId(userId);
      // If the cart doesn't exist, return a 404 error
      if (cart == null) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      LocalDateTime localDateTime = LocalDateTime.now();
      String[] splitTimestamp = localDateTime.toString().split("T");
      String date = splitTimestamp[0];
      String timeAndZone = splitTimestamp[1];


      // Create a new order
      Order order = new Order(userId, cart.getCartItems(), cart.getTotal(), shippingAddress, Order.OrderStatus.Processing);
      // Save the order
      order.setOrderDate(date);
      order.setOrderTime(timeAndZone);
      orderRepository.save(order);

      // Clear the user's cart
      cart.clearCart();
      cartRepository.save(cart);
      return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
  }

  public ResponseEntity<Order> showOrder(String userId) {
      // Retrieve the user's order
      Order order = orderRepository.findFirstByUserIdOrderByLastUpdatedDesc(userId);

      // If the order doesn't exist, return a 404 error
      if (order == null) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      List<CartItem> cartItems = order.getCartItems();
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
      return new ResponseEntity<>(order, HttpStatus.OK);
  }


}
