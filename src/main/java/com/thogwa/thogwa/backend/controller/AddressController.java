package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.model.Address;
import com.thogwa.thogwa.backend.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddressController {
    @Autowired
    AddressService addressService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/address/{email}")
    @ResponseBody
    public ResponseEntity<String> addAddress(@RequestBody Address address,@PathVariable String email) {
        try {
            addressService.addAddress(address, email);
            LOGGER.info(email.toString());
            return new ResponseEntity<>("Address added successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to add address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
