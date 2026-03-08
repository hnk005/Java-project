package com.example.quanlykho.dao;

import com.example.quanlykho.model.ticket.ExportDetail;
import com.example.quanlykho.model.ticket.ExportTicket;

import java.sql.SQLException;
import java.util.List;

public class ExportTicketDAO {
    // TODO: Implement using DatabaseConnection.getConnection()

    public List<ExportTicket> findAll() throws SQLException {
        // TODO: SELECT et.*, a.full_name
        //       FROM export_ticket et
        //       JOIN account a ON et.account_id = a.account_id
        //       ORDER BY et.create_at DESC
        return null;
    }

    public ExportTicket findById(int exportId) throws SQLException {
        // TODO: SELECT * FROM export_ticket WHERE export_id = ?
        return null;
    }

    public int insert(ExportTicket ticket) throws SQLException {
        // TODO: INSERT INTO export_ticket (account_id, total_amount, create_at) VALUES (?, ?, ?)
        //       Return the generated key (export_id)
        return -1;
    }

    public boolean insertDetail(ExportDetail detail) throws SQLException {
        // TODO: INSERT INTO export_detail (export_id, product_id, quantity, export_price) VALUES (?, ?, ?, ?)
        return false;
    }

    public List<ExportDetail> getDetails(int exportId) throws SQLException {
        // TODO: SELECT ed.*, p.product_name
        //       FROM export_detail ed
        //       JOIN product p ON ed.product_id = p.product_id
        //       WHERE ed.export_id = ?
        return null;
    }
}

