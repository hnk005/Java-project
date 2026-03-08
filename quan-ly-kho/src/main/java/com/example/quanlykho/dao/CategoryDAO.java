package com.example.quanlykho.dao;

import com.example.quanlykho.model.category.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO {
    // TODO: Implement using DatabaseConnection.getConnection()

    public List<Category> findAll() throws SQLException {
        // TODO: SELECT * FROM category
        return null;
    }

    public Category findById(int categoryId) throws SQLException {
        // TODO: SELECT * FROM category WHERE category_id = ?
        return null;
    }

    public boolean insert(Category category) throws SQLException {
        // TODO: INSERT INTO category (category_name, description) VALUES (?, ?)
        return false;
    }

    public boolean update(Category category) throws SQLException {
        // TODO: UPDATE category SET category_name=?, description=? WHERE category_id=?
        return false;
    }

    public boolean delete(int categoryId) throws SQLException {
        // TODO: DELETE FROM category WHERE category_id = ?
        return false;
    }
}

