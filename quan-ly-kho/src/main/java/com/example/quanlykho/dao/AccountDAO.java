package com.example.quanlykho.dao;

import com.example.quanlykho.model.account.Account;
import com.example.quanlykho.model.account.Role;
import com.example.quanlykho.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private final Connection db;

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
        PreparedStatement stmt = db.prepareStatement(
                "SELECT account_id, username, full_name, role, status FROM account ORDER BY account_id");
        ResultSet rs = stmt.executeQuery();
        List<Account> accounts = new ArrayList<>();
        while (rs.next()) {
            Account account = new Account();
            account.setAccountId(rs.getInt("account_id"));
            account.setUsername(rs.getString("username"));
            account.setFullName(rs.getString("full_name"));
            String roleStr = rs.getString("role");
            if (roleStr != null) account.setRole(Role.valueOf(roleStr.toUpperCase()));
            account.setStatus(rs.getBoolean("status"));
            accounts.add(account);
        }
        return accounts;
    }

    public boolean insert(Account account) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(
                "INSERT INTO account (username, password, full_name, role, status) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, account.getUsername());
        stmt.setString(2, account.getPassword());
        stmt.setString(3, account.getFullName());
        stmt.setString(4, account.getRole() != null ? account.getRole().name() : null);
        stmt.setBoolean(5, account.isStatus());
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean update(Account account) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(
                "UPDATE account SET full_name=?, role=?, status=? WHERE account_id=?");
        stmt.setString(1, account.getFullName());
        stmt.setString(2, account.getRole() != null ? account.getRole().name() : null);
        stmt.setBoolean(3, account.isStatus());
        stmt.setInt(4, account.getAccountId());
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean updatePassword(int accountId, String newPassword) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(
                "UPDATE account SET password=? WHERE account_id=?");
        stmt.setString(1, newPassword);
        stmt.setInt(2, accountId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean setStatus(int accountId, boolean status) throws SQLException {
        PreparedStatement stmt = db.prepareStatement(
                "UPDATE account SET status=? WHERE account_id=?");
        stmt.setBoolean(1, status);
        stmt.setInt(2, accountId);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }
}
