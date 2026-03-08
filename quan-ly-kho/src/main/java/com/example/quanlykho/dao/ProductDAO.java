package com.example.quanlykho.dao;

import com.example.quanlykho.model.product.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductDAO {
    // TODO: Implement using DatabaseConnection.getConnection()

    public List<Product> findAll() throws SQLException {
        // TODO: SELECT p.*, c.category_name FROM product p LEFT JOIN category c ON p.category_id = c.category_id
        return null;
    }

    public Product findById(int productId) throws SQLException {
        // TODO: SELECT * FROM product WHERE product_id = ?
        return null;
    }

    public boolean insert(Product product) throws SQLException {
        // TODO: INSERT INTO product (product_name, category_id, quantity, price, image_url) VALUES (?, ?, ?, ?, ?)
        return false;
    }

    public boolean update(Product product) throws SQLException {
        // TODO: UPDATE product SET product_name=?, category_id=?, quantity=?, price=?, image_url=? WHERE product_id=?
        return false;
    }

    public boolean delete(int productId) throws SQLException {
        // TODO: DELETE FROM product WHERE product_id = ?
        return false;
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        // TODO: SELECT * FROM product WHERE quantity <= ?
        return null;
    }
}

