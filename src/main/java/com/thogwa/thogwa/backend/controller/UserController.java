package com.thogwa.thogwa.backend.controller;


import com.thogwa.thogwa.backend.dto.user.SignInDto;
import com.thogwa.thogwa.backend.dto.user.SignupDto;
import com.thogwa.thogwa.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("user")
@Controller
public class UserController {
    @Autowired
    UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/signup")
    public void handleSignup(@RequestBody SignupDto signupDto) {
        // Perform signup logic here

        // You can save the user to the database, send a confirmation email, etc.
            userService.signUp(signupDto);
        LOGGER.info("Received signup data: {}", signupDto.toString());
        // For simplicity, let's just print the received data to the console
        System.out.println("Received signup data: " + signupDto);


        // Redirect to a success page or return a response as needed

    }

    @PostMapping("/login")
    public String handleLogin(@RequestBody SignInDto loginDto, RedirectAttributes redirectAttributes) {
        // Your login logic goes here
        // Validate the user's credentials, check against the database, etc.

        // For simplicity, let's just print the received data to the console
        System.out.println("Received login data: " + loginDto);
        // Assuming the login is successful, add a redirect attribute
        redirectAttributes.addFlashAttribute("loginSuccess", true);

        // Redirect to the checkout view
        return "redirect:/checkout";
    }

}
