package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Categoryrepository extends JpaRepository<Category,Integer> {
    Category findByCategoryName(String categoryName);
}
