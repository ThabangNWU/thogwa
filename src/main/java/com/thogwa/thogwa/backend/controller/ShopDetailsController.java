package com.thogwa.thogwa.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopDetailsController {
//    @GetMapping("/shop-details")
//    public String showShopDetails(@RequestParam("productId") Integer productId, Model model) {
//        // Logic to fetch product details based on productId
//        // ...
//
//        // Add product details to the model
//        model.addAttribute("productId", productId);
//        // Add other product details as needed
//
//        return "shop-details"; // This corresponds to the Thymeleaf HTML file
//    }
    @GetMapping("/shop-details")
    public String showShopDetails() {
        return "shop-details";
    }
}
