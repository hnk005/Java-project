package com.example.quanlykho.dao;

import com.example.quanlykho.model.category.Category;
import com.example.quanlykho.model.product.Product;
import com.example.quanlykho.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final Connection db;

    public ProductDAO() {
        db = DatabaseConnection.getConnection();
    }

    public List<Product> findAll() throws SQLException {

        PreparedStatement stmt = db.prepareStatement("SELECT p.*, c.category_id, c.category_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id");
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            Product product = new Product();
            Category category = new Category();

            category.setCategoryId(rs.getInt("category_id"));
            category.setCategoryName(rs.getString("category_name"));

            product.setProductId(rs.getInt("product_id"));
            product.setProductName(rs.getString("product_name"));
            product.setQuantity(rs.getInt("quantity"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setImageUrl(rs.getString("image_url"));
            product.setCategory(category);

            products.add(product);
        }
        return products;
    }

    public Product findById(int productId) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("SELECT p.*, c.category_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id WHERE p.product_id = ?");
        stmt.setInt(1, productId);
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();
        if (rs.next()) {
            Product product = new Product();
            Category category = new Category();

            category.setCategoryId(rs.getInt("category_id"));
            category.setCategoryName(rs.getString("category_name"));

            product.setProductId(rs.getInt("product_id"));
            product.setProductName(rs.getString("product_name"));
            product.setQuantity(rs.getInt("quantity"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setImageUrl(rs.getString("image_url"));
            product.setCategory(category);
            return product;
        }
        return null;
    }

    public boolean insert(Product product) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("INSERT INTO product (product_name, category_id, quantity, price, image_url) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, product.getProductName());
        if (product.getCategory() != null) {
            stmt.setInt(2, product.getCategory().getCategoryId());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setInt(3, product.getQuantity());
        stmt.setBigDecimal(4, product.getPrice());
        stmt.setString(5, product.getImageUrl());
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean update(Product product) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("UPDATE product SET product_name=?, category_id=?, quantity=?, price=?, image_url=? WHERE product_id=?");
        stmt.setString(1, product.getProductName());
        if (product.getCategory() != null) {
            stmt.setInt(2, product.getCategory().getCategoryId());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setInt(3, product.getQuantity());
        stmt.setBigDecimal(4, product.getPrice());
        stmt.setString(5, product.getImageUrl());
        stmt.setInt(6, product.getProductId());
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean delete(int productId) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("DELETE FROM product WHERE product_id = ?");
        stmt.setInt(1, productId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("SELECT p.*, c.category_id, c.category_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id WHERE p.quantity < ?");
        stmt.setInt(1, threshold);
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            Product product = new Product();
            Category category = new Category();

            category.setCategoryId(rs.getInt("category_id"));
            category.setCategoryName(rs.getString("category_name"));

            product.setProductId(rs.getInt("product_id"));
            product.setProductName(rs.getString("product_name"));
            product.setQuantity(rs.getInt("quantity"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setImageUrl(rs.getString("image_url"));
            product.setCategory(category);
            products.add(product);
        }
        return products;
    }
}

