package com.example.quanlykho.controller;

import com.example.quanlykho.bus.DashboardBus;
import com.example.quanlykho.model.dashboard.DashboardStats;
import com.example.quanlykho.model.product.Product;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {

    @FXML
    private Label totalProductsLabel;
    @FXML
    private Label totalImportsLabel;
    @FXML
    private Label totalExportsLabel;
    @FXML
    private Label totalRevenueLabel;

    @FXML
    private TableView<Product> lowStockTable;
    @FXML
    private TableColumn<Product, String> colProductName;
    @FXML
    private TableColumn<Product, String> colCategoryName;
    @FXML
    private TableColumn<Product, Integer> colQuantity;
    @FXML
    private TableColumn<Product, BigDecimal> colPrice;

    private final DashboardBus dashboardBus = new DashboardBus();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadStats();
    }

    private void setupTable() {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void loadStats() {
        try {
            DashboardStats stats = dashboardBus.getStats();
            NumberFormat currencyFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            totalProductsLabel.setText(String.valueOf(stats.getTotalProducts()));
            totalImportsLabel.setText(String.valueOf(stats.getTotalImports()));
            totalExportsLabel.setText(String.valueOf(stats.getTotalExports()));
            BigDecimal rev = stats.getTotalRevenue();
            totalRevenueLabel.setText(rev != null ? currencyFmt.format(rev) + " ₫" : "0 ₫");

            List<Product> lowStock = stats.getLowStockProducts();
            if (lowStock != null) {
                lowStockTable.setItems(FXCollections.observableArrayList(lowStock));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

