package com.example.quanlykho.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quan_ly_kho";
    private static final String USER = "root";
    private static final String PASSWORD = "kha512020";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Kết nối MySQL thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver JDBC!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Database: " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối Database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
