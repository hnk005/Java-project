package com.example.quanlykho.bus;

import com.example.quanlykho.dao.DashboardDAO;
import com.example.quanlykho.model.dashboard.DashboardStats;

import java.sql.SQLException;

public class DashboardBus {
    private static final int LOW_STOCK_THRESHOLD = 10;
    private final DashboardDAO dashboardDAO;

    public DashboardBus() {
        dashboardDAO = new DashboardDAO();
    }

    public DashboardStats getStats() throws SQLException {
        DashboardStats stats = new DashboardStats();
        stats.setTotalProducts(dashboardDAO.countProducts());
        stats.setTotalImports(dashboardDAO.countImports());
        stats.setTotalExports(dashboardDAO.countExports());
        stats.setTotalRevenue(dashboardDAO.getTotalRevenue());
        stats.setLowStockProducts(dashboardDAO.getLowStockProducts(LOW_STOCK_THRESHOLD));
        return stats;
    }
}

