package com.example.quanlykho.dao;

import com.example.quanlykho.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    private Connection db;

    public AccountDAO() {
        db = DatabaseConnection.getConnection();
    }

    //    public List<Account> findAll() {
//        if(db == null) {
//            return null;
//        }
//
//        try (PreparedStatement pstmt = db.prepareStatement("SELECT * FROM account")) {
//            ResultSet rs = pstmt.executeQuery();
//            List<Account> accounts = new ArrayList<>();
//            while (rs.next()) {
//                Account account = new Account();
//                account.setAccountId(rs.getInt("account_id"));
//                account.setUsername(rs.getString("username"));
//                account.setPassword(rs.getString("password"));
//                account.setFullName(rs.getString("full_name"));
//
//                String roleStr = rs.getString("role");
//                Role role = null;
//                if (roleStr != null) {
//                    role = Role.valueOf(roleStr.toUpperCase());
//                }
//                account.setRole(role);
//
//                account.setStatus(rs.getBoolean("status"));
//
//                accounts.add(account);
//            }
//
//            return accounts;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
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
}
