package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer> {
//    List<SubCategory> findByCategory_CategoryId(Integer categoryId);
}
