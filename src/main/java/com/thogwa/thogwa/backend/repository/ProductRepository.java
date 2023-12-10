package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Category;
import com.thogwa.thogwa.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameLike(String keyword, Pageable pageDetails);
    Page<Product> findByCategory(Category category, Pageable pageable);

//    SELECT product_id, description, discount, imageurl, name, price, quantity, special_price, category_id
//    FROM public.products;
        List<Product> findAllByCategoryId(Integer category);
}