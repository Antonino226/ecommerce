package com.youtube.ecommerce.entity;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    
    private String categoryName;
    
    private String categoryDescription;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "category_images",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private Set<ImageModel> categoryImages;

    @OneToMany(mappedBy = "category")  // mappedBy should refer to the field in Product that maps back to Category
    private List<Product> products;


    // Getters and Setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Set<ImageModel> getCategoryImages() {
        return categoryImages;
    }

    public void setCategoryImages(Set<ImageModel> categoryImages) {
        this.categoryImages = categoryImages;
    }


}
