package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.common.ApiResponse;
import com.thogwa.thogwa.backend.dto.cart.AddToCartDto;
import com.thogwa.thogwa.backend.dto.cart.CartDto;
import com.thogwa.thogwa.backend.model.Cart;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.UserRepository;
import com.thogwa.thogwa.backend.service.AuthenticationService;
import com.thogwa.thogwa.backend.service.CartService;
import com.thogwa.thogwa.backend.service.CategoryService;
import com.thogwa.thogwa.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CartController.class);

    @PostMapping("/add/cart/item/{email}")
    public ResponseEntity<ApiResponse> handleCart(@RequestBody List<Cart> userCart,@PathVariable String email) {
        logger.info(email);

        try {
            cartService.saveCartItems(userCart,email);
            return new ResponseEntity<>(new ApiResponse(true, "Cart items saved successfully"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error saving cart items", e);
            return new ResponseEntity<>(new ApiResponse(false, "Failed to save cart items. Please try again."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
