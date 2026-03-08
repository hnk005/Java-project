package com.example.quanlykho.bus;

import com.example.quanlykho.dao.AccountDAO;
import com.example.quanlykho.util.JavaFXUtil;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AccountBus {
    private final AccountDAO accountDAO;

    public AccountBus() {
        accountDAO = new AccountDAO();
    }

    public boolean login(String username, String password) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        return accountDAO.compareAccount(username, password);
    }
}
