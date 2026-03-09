package com.example.quanlykho.dao;

import com.example.quanlykho.model.product.Product;
import com.example.quanlykho.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class DashboardDAO {
    // TODO: Implement using DatabaseConnection.getConnection()

    public int countProducts() throws SQLException {
        // TODO: SELECT COUNT(*) FROM product
        return 0;
    }

    public int countImports() throws SQLException {
        // TODO: SELECT COUNT(*) FROM import_ticket
        return 0;
    }

    public int countExports() throws SQLException {
        // TODO: SELECT COUNT(*) FROM export_ticket
        return 0;
    }

    public BigDecimal getTotalRevenue() throws SQLException {
        // TODO: SELECT COALESCE(SUM(total_amount), 0) FROM export_ticket
        return BigDecimal.ZERO;
    }

    public List<Product> getLowStockProducts(int threshold) throws SQLException {

        return null;
    }
}

