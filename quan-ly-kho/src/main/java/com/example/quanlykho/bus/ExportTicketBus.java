package com.example.quanlykho.bus;

import com.example.quanlykho.dao.ExportTicketDAO;
import com.example.quanlykho.model.ticket.ExportDetail;
import com.example.quanlykho.model.ticket.ExportTicket;

import java.sql.SQLException;
import java.util.List;

public class ExportTicketBus {
    private final ExportTicketDAO exportTicketDAO;

    public ExportTicketBus() {
        exportTicketDAO = new ExportTicketDAO();
    }

    public List<ExportTicket> getAll() throws SQLException {
        return exportTicketDAO.findAll();
    }

    public ExportTicket getById(int exportId) throws SQLException {
        return exportTicketDAO.findById(exportId);
    }

    public int create(ExportTicket ticket, List<ExportDetail> details) throws SQLException {
        if (details == null || details.isEmpty()) return -1;
        int exportId = exportTicketDAO.insert(ticket);
        if (exportId > 0) {
            for (ExportDetail detail : details) {
                detail.setExportId(exportId);
                exportTicketDAO.insertDetail(detail);
            }
        }
        return exportId;
    }

    public List<ExportDetail> getDetails(int exportId) throws SQLException {
        return exportTicketDAO.getDetails(exportId);
    }
}

