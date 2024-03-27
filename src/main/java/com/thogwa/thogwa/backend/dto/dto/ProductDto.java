package com.thogwa.thogwa.backend.dto.dto;


import com.thogwa.thogwa.backend.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private double costPrice;
    private double salePrice;
    private String image;
    private Category category;
    private boolean activated;
    private boolean deleted;
    private String currentPage;
}
