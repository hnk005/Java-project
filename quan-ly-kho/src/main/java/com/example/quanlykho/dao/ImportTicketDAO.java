package com.example.quanlykho.dao;

import com.example.quanlykho.model.ticket.ImportDetail;
import com.example.quanlykho.model.ticket.ImportTicket;

import java.sql.SQLException;
import java.util.List;

public class ImportTicketDAO {
    // TODO: Implement using DatabaseConnection.getConnection()

    public List<ImportTicket> findAll() throws SQLException {
        // TODO: SELECT it.*, s.supplier_name, a.full_name
        //       FROM import_ticket it
        //       JOIN supplier s ON it.supplier_id = s.supplier_id
        //       JOIN account a ON it.account_id = a.account_id
        //       ORDER BY it.create_at DESC
        return null;
    }

    public ImportTicket findById(int importId) throws SQLException {
        // TODO: SELECT * FROM import_ticket WHERE import_id = ?
        return null;
    }

    public int insert(ImportTicket ticket) throws SQLException {
        // TODO: INSERT INTO import_ticket (supplier_id, account_id, total_amount, create_at) VALUES (?, ?, ?, ?)
        //       Return the generated key (import_id)
        return -1;
    }

    public boolean insertDetail(ImportDetail detail) throws SQLException {
        // TODO: INSERT INTO import_detail (import_id, product_id, quantity, import_price) VALUES (?, ?, ?, ?)
        return false;
    }

    public List<ImportDetail> getDetails(int importId) throws SQLException {
        // TODO: SELECT id.*, p.product_name
        //       FROM import_detail id
        //       JOIN product p ON id.product_id = p.product_id
        //       WHERE id.import_id = ?
        return null;
    }
}

