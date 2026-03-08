package com.example.quanlykho.dao;

import com.example.quanlykho.model.account.Account;
import com.example.quanlykho.model.account.Role;
import com.example.quanlykho.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDAO {
    private Connection db;

    public AccountDAO() {
        db = DatabaseConnection.getConnection();
    }

    public boolean compareAccount(String username, String password) throws SQLException {
        PreparedStatement stmt = db.prepareStatement("SELECT username, password FROM account WHERE username = ? and password = ?");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("username").equals(username) &&
                    rs.getString("password").equals(password);
        }
        return false;
    }

    public Account findByUsernameAndPassword(String username, String password) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM account WHERE username = ? AND password = ? AND status = TRUE");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Account account = new Account();
            account.setAccountId(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));
            account.setPassword(rs.getString("password"));
            account.setFullName(rs.getString("full_name"));
            String roleStr = rs.getString("role");
            if (roleStr != null) account.setRole(Role.valueOf(roleStr.toUpperCase()));
            account.setStatus(rs.getBoolean("status"));
            return account;
        }
        return null;
    }

    public List<Account> findAll() throws SQLException {
        // TODO: SELECT * FROM account ORDER BY account_id
        return null;
    }

    public boolean insert(Account account) throws SQLException {
        // TODO: INSERT INTO account (username, password, full_name, role, status) VALUES (?, ?, ?, ?, ?)
        return false;
    }

    public boolean update(Account account) throws SQLException {
        // TODO: UPDATE account SET username=?, full_name=?, role=?, status=? WHERE account_id=?
        return false;
    }

    public boolean updatePassword(int accountId, String newPassword) throws SQLException {
        // TODO: UPDATE account SET password=? WHERE account_id=?
        return false;
    }

    public boolean setStatus(int accountId, boolean status) throws SQLException {
        // TODO: UPDATE account SET status=? WHERE account_id=?
        return false;
    }
}
