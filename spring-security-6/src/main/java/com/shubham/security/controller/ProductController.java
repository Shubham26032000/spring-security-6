package com.shubham.security.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private record Product(Integer productId, String productName, double price){}

    List<Product> products = new ArrayList<>(List.of(
            new Product(1,"Android",20000.0),
            new Product(2,"Iphone",2020200.00)
    ));

    @GetMapping
    public List<Product> getProducts(){
        return products;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product){
        products.add(product);
        return product;
    }
}
