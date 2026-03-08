package com.example.quanlykho.bus;

import com.example.quanlykho.dao.AccountDAO;
import com.example.quanlykho.model.account.Account;
import com.example.quanlykho.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class AccountBus {
    private final AccountDAO accountDAO;

    public AccountBus() {
        accountDAO = new AccountDAO();
    }

    public boolean login(String username, String password) throws SQLException {
        if (username.isEmpty() || password.isEmpty()) return false;
        Account account = accountDAO.findByUsernameAndPassword(username, password);
        if (account != null) {
            SessionManager.getInstance().setCurrentAccount(account);
            return true;
        }
        return false;
    }

    public List<Account> getAll() throws SQLException {
        return accountDAO.findAll();
    }

    public boolean create(Account account) throws SQLException {
        if (account.getUsername() == null || account.getUsername().isBlank()) return false;
        if (account.getPassword() == null || account.getPassword().isBlank()) return false;
        if (account.getFullName() == null || account.getFullName().isBlank()) return false;
        if (account.getRole() == null) return false;
        return accountDAO.insert(account);
    }

    public boolean update(Account account) throws SQLException {
        if (account.getAccountId() <= 0) return false;
        if (account.getFullName() == null || account.getFullName().isBlank()) return false;
        return accountDAO.update(account);
    }

    public boolean toggleStatus(int accountId, boolean status) throws SQLException {
        return accountDAO.setStatus(accountId, status);
    }

    public boolean updatePassword(int accountId, String newPassword) throws SQLException {
        if (newPassword == null || newPassword.isBlank()) return false;
        return accountDAO.updatePassword(accountId, newPassword);
    }
}
