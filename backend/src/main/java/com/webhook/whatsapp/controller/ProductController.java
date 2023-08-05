package com.webhook.whatsapp.controller;


import com.webhook.whatsapp.entity.Product;
import com.webhook.whatsapp.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    //for getting all products
    @Operation(summary = "This is to fetch all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    //for getting products by id
    @Operation(summary = "This is to fetch product by productId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    //for adding product
    @Operation(summary = "This is to add product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product added successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed to add product from the details you provided",
                    content = @Content)
    })
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    //for modifying product
    @Operation(summary = "This is to modify content of product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable String productId, @RequestBody Product product) {
        Product existingProduct = productService.getProductById(productId);
        if (existingProduct == null) {
            return null;
        }
        // update existing product fields with the values from the request body
        existingProduct.setProduct(product.getProduct());
        existingProduct.setSale_price(product.getSale_price());
        return productService.saveProduct(existingProduct);
    }

    @Operation(summary = "This is to delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Failed",
                    content = @Content)
    })
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
    }



}
