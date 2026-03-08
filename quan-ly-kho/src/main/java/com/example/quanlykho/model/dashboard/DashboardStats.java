package com.example.quanlykho.model.dashboard;

import com.example.quanlykho.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public class DashboardStats {
    private int totalProducts;
    private int totalImports;
    private int totalExports;
    private BigDecimal totalRevenue;
    private List<Product> lowStockProducts;

    public DashboardStats() {
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalImports() {
        return totalImports;
    }

    public void setTotalImports(int totalImports) {
        this.totalImports = totalImports;
    }

    public int getTotalExports() {
        return totalExports;
    }

    public void setTotalExports(int totalExports) {
        this.totalExports = totalExports;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<Product> getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(List<Product> lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }
}

