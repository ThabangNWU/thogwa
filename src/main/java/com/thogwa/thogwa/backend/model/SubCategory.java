package com.thogwa.thogwa.backend.model;

import jakarta.persistence.*;
@Entity
@Table(name = "subcategories")
public class SubCategory {
    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Integer subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subcategoryId;
    private String name;
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

}

