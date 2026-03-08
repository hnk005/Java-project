package com.example.quanlykho.bus;

import com.example.quanlykho.dao.ImportTicketDAO;
import com.example.quanlykho.model.ticket.ImportDetail;
import com.example.quanlykho.model.ticket.ImportTicket;

import java.sql.SQLException;
import java.util.List;

public class ImportTicketBus {
    private final ImportTicketDAO importTicketDAO;

    public ImportTicketBus() {
        importTicketDAO = new ImportTicketDAO();
    }

    public List<ImportTicket> getAll() throws SQLException {
        return importTicketDAO.findAll();
    }

    public ImportTicket getById(int importId) throws SQLException {
        return importTicketDAO.findById(importId);
    }

    public int create(ImportTicket ticket, List<ImportDetail> details) throws SQLException {
        if (ticket.getSupplierId() <= 0) return -1;
        if (details == null || details.isEmpty()) return -1;
        int importId = importTicketDAO.insert(ticket);
        if (importId > 0) {
            for (ImportDetail detail : details) {
                detail.setImportId(importId);
                importTicketDAO.insertDetail(detail);
            }
        }
        return importId;
    }

    public List<ImportDetail> getDetails(int importId) throws SQLException {
        return importTicketDAO.getDetails(importId);
    }
}

