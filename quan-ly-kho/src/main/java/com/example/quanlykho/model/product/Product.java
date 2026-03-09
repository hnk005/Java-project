package com.example.quanlykho.model.product;

import com.example.quanlykho.model.category.Category;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private String imageUrl;
    private Category category;

    public Product() {
    }

    public Product(int productId, String productName, int quantity, BigDecimal price, String imageUrl, Category category) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}

