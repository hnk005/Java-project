package com.example.quanlykho.bus;

import com.example.quanlykho.dao.CategoryDAO;
import com.example.quanlykho.model.category.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryBus {
    private final CategoryDAO categoryDAO;

    public CategoryBus() {
        categoryDAO = new CategoryDAO();
    }

    public List<Category> getAll() throws SQLException {
        return categoryDAO.findAll();
    }

    public boolean create(Category category) throws SQLException {
        if (category.getCategoryName() == null || category.getCategoryName().isBlank()) return false;
        return categoryDAO.insert(category);
    }

    public boolean update(Category category) throws SQLException {
        if (category.getCategoryId() <= 0) return false;
        if (category.getCategoryName() == null || category.getCategoryName().isBlank()) return false;
        return categoryDAO.update(category);
    }

    public boolean delete(int categoryId) throws SQLException {
        if (categoryId <= 0) return false;
        return categoryDAO.delete(categoryId);
    }
}

