package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.dto.cart.AddToCartDto;
import com.thogwa.thogwa.backend.dto.cart.CartDto;
import com.thogwa.thogwa.backend.dto.cart.CartItemDto;
import com.thogwa.thogwa.backend.exception.CustomException;
import com.thogwa.thogwa.backend.model.Cart;
import com.thogwa.thogwa.backend.model.Product;
import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.repository.CartRepository;
import com.thogwa.thogwa.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    ProductService productService;

    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;

    public void saveCartItems(List<Cart> cartItems, String email) {

        List<Cart> cartList = new ArrayList<>();

        for (int i = 0; i < cartItems.size(); i++) {
            Cart cart = new Cart();  // Create a new instance for each iteration
            cart.setEmail(email);
            cart.setProductName(cartItems.get(i).getProductName());
            cart.setProductImageName(cartItems.get(i).getProductImageName());
            cart.setProduct(cartItems.get(i).getProduct());
            cart.setCategoryID(cartItems.get(i).getCategoryID());
            cart.setQuantity(cartItems.get(i).getQuantity());
            cartList.add(cart);
        }

        cartRepository.saveAll(cartList);
    }

//    public List<Cart>


}
