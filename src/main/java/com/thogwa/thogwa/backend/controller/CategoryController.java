package com.thogwa.thogwa.backend.controller;

import com.thogwa.thogwa.backend.common.ApiResponse;
import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.listCategories();
        model.addAttribute("categories", categories);
        return "categories"; // Thymeleaf template name (e.g., categories.html)
    }

    @GetMapping("/create-form")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "createCategory"; // Thymeleaf template name (e.g., createCategory.html)
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createCategory"; // Return to the form with validation errors
        }

        if (Objects.nonNull(categoryService.readCategory(category.getCategoryName()))) {
            model.addAttribute("error", "Category already exists");
            return "createCategory";
        }

        categoryService.createCategory(category);
        model.addAttribute("success", "Category created successfully");
        return "redirect:/category/create-form"; // Redirect to the create form
    }


    @GetMapping("/update/{categoryID}")
    public String showUpdateCategoryForm(@PathVariable Integer categoryID, Model model) {
        Category category = categoryService.readCategory(categoryID).orElse(null);
        if (category == null) {
            // Handle the case where the category is not found
            return "error"; // You may want to create a custom error page
        }

        model.addAttribute("category", category);
        return "update-category";
    }

    @PostMapping("/update/{categoryID}")
    public String updateCategory(@PathVariable Integer categoryID, @Valid @ModelAttribute Category updatedCategory,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the update form
            model.addAttribute("category", updatedCategory);
            return "update-category";
        }

        // Check if the category exists before updating
        Category existingCategory = categoryService.readCategory(categoryID).orElse(null);
        if (existingCategory == null) {
            // Handle the case where the category is not found
            return "error"; // You may want to create a custom error page
        }

        // Update the existing category
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        existingCategory.setDescription(updatedCategory.getDescription());
        existingCategory.setImageUrl(updatedCategory.getImageUrl());

        categoryService.updateCategory(categoryID, existingCategory);
        model.addAttribute("success", "Category updated successfully");

        return "update-category";
    }

}
