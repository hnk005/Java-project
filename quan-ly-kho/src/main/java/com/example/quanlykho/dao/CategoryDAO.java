package com.example.quanlykho.dao;

import com.example.quanlykho.model.category.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final Connection db;

    public CategoryDAO() {
        db = com.example.quanlykho.util.DatabaseConnection.getConnection();
    }

    public List<Category> findAll() throws SQLException {
        PreparedStatement stmt = db.prepareStatement("SELECT * FROM category");
        stmt.executeQuery();
        ResultSet rs = stmt.getResultSet();

        List<Category> categories = new ArrayList<>();
        while (rs.next()) {
            Category category = new Category();
            category.setCategoryId(rs.getInt("category_id"));
            category.setCategoryName(rs.getString("category_name"));
            category.setDescription(rs.getString("description"));
            categories.add(category);
        }
        return categories;
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

