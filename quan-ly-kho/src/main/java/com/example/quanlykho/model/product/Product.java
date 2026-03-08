package com.example.quanlykho.model.product;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String productName;
    private int categoryId;
    private String categoryName; // for display
    private int quantity;
    private BigDecimal price;
    private String imageUrl;

    public Product() {
    }

    public Product(int productId, String productName, int categoryId, int quantity, BigDecimal price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}

