package com.thogwa.thogwa.backend.service;

import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.SubCategory;
import com.thogwa.thogwa.backend.repository.Categoryrepository;
import com.thogwa.thogwa.backend.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private Categoryrepository categoryrepository;


    private void addSubCategory(Integer categoryId, String name) {
        Category category = categoryrepository.findById(categoryId).get();
        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(category);
        subCategory.setName(name);
    }

}
