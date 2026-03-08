package com.example.quanlykho.bus;

import com.example.quanlykho.dao.ProductDAO;
import com.example.quanlykho.model.product.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductBus {
    private final ProductDAO productDAO;

    public ProductBus() {
        productDAO = new ProductDAO();
    }

    public List<Product> getAll() throws SQLException {
        return productDAO.findAll();
    }

    public Product getById(int productId) throws SQLException {
        return productDAO.findById(productId);
    }

    public boolean create(Product product) throws SQLException {
        if (product.getProductName() == null || product.getProductName().isBlank()) return false;
        if (product.getPrice() == null) return false;
        return productDAO.insert(product);
    }

    public boolean update(Product product) throws SQLException {
        if (product.getProductId() <= 0) return false;
        if (product.getProductName() == null || product.getProductName().isBlank()) return false;
        return productDAO.update(product);
    }

    public boolean delete(int productId) throws SQLException {
        if (productId <= 0) return false;
        return productDAO.delete(productId);
    }

    public List<Product> getLowStock(int threshold) throws SQLException {
        return productDAO.findLowStock(threshold);
    }
}

