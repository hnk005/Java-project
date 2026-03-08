package com.example.quanlykho.dao;

import com.example.quanlykho.model.supplier.Supplier;

import java.sql.SQLException;
import java.util.List;

public class SupplierDAO {
    // TODO: Implement using DatabaseConnection.getConnection()

    public List<Supplier> findAll() throws SQLException {
        // TODO: SELECT * FROM supplier
        return null;
    }

    public Supplier findById(int supplierId) throws SQLException {
        // TODO: SELECT * FROM supplier WHERE supplier_id = ?
        return null;
    }

    public boolean insert(Supplier supplier) throws SQLException {
        // TODO: INSERT INTO supplier (supplier_name, phone, address) VALUES (?, ?, ?)
        return false;
    }

    public boolean update(Supplier supplier) throws SQLException {
        // TODO: UPDATE supplier SET supplier_name=?, phone=?, address=? WHERE supplier_id=?
        return false;
    }

    public boolean delete(int supplierId) throws SQLException {
        // TODO: DELETE FROM supplier WHERE supplier_id = ?
        return false;
    }
}

