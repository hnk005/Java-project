package com.example.quanlykho.bus;

import com.example.quanlykho.dao.SupplierDAO;
import com.example.quanlykho.model.supplier.Supplier;

import java.sql.SQLException;
import java.util.List;

public class SupplierBus {
    private final SupplierDAO supplierDAO;

    public SupplierBus() {
        supplierDAO = new SupplierDAO();
    }

    public List<Supplier> getAll() throws SQLException {
        return supplierDAO.findAll();
    }

    public boolean create(Supplier supplier) throws SQLException {
        if (supplier.getSupplierName() == null || supplier.getSupplierName().isBlank()) return false;
        if (supplier.getPhone() == null || supplier.getPhone().isBlank()) return false;
        return supplierDAO.insert(supplier);
    }

    public boolean update(Supplier supplier) throws SQLException {
        if (supplier.getSupplierId() <= 0) return false;
        if (supplier.getSupplierName() == null || supplier.getSupplierName().isBlank()) return false;
        return supplierDAO.update(supplier);
    }

    public boolean delete(int supplierId) throws SQLException {
        if (supplierId <= 0) return false;
        return supplierDAO.delete(supplierId);
    }
}

