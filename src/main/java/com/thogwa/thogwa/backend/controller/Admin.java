package com.thogwa.thogwa.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class Admin {
    @GetMapping("/admin")
    public String adminHome () {
        return "admin/adminHome";
    }
}
