package com.example.quanlykho.controller;

import com.example.quanlykho.bus.ExportTicketBus;
import com.example.quanlykho.bus.ProductBus;
import com.example.quanlykho.model.product.Product;
import com.example.quanlykho.model.ticket.ExportDetail;
import com.example.quanlykho.model.ticket.ExportTicket;
import com.example.quanlykho.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExportController implements Initializable {

    @FXML
    private TableView<ExportTicket> exportTable;
    @FXML
    private TableColumn<ExportTicket, Integer> colId;
    @FXML
    private TableColumn<ExportTicket, String> colAccount;
    @FXML
    private TableColumn<ExportTicket, BigDecimal> colTotal;
    @FXML
    private TableColumn<ExportTicket, String> colDate;

    private final ExportTicketBus exportBus = new ExportTicketBus();
    private final ProductBus productBus = new ProductBus();
    private final NumberFormat currFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private final DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("exportId"));
        colAccount.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colDate.setCellValueFactory(data -> {
            LocalDateTime dt = data.getValue().getCreateAt();
            return new SimpleStringProperty(dt != null ? dtFmt.format(dt) : "");
        });
        colTotal.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : currFmt.format(v) + " ₫");
            }
        });
        exportTable.setRowFactory(tv -> {
            TableRow<ExportTicket> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) showDetails(row.getItem());
            });
            return row;
        });
    }

    private void loadData() {
        try {
            List<ExportTicket> list = exportBus.getAll();
            exportTable.setItems(list != null ? FXCollections.observableArrayList(list) : FXCollections.observableArrayList());
        } catch (Exception e) {
            showError("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void onRefresh() {
        loadData();
    }

    @FXML
    private void onAdd() {
        try {
            List<Product> products = productBus.getAll();
            if (products == null || products.isEmpty()) {
                showWarning("Chưa có sản phẩm.");
                return;
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Tạo phiếu xuất kho");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().setPrefWidth(600);

            VBox root = new VBox(10);
            root.setPadding(new Insets(15));

            TableView<ExportDetail> detailTable = new TableView<>();
            TableColumn<ExportDetail, String> dcProd = new TableColumn<>("Sản phẩm");
            TableColumn<ExportDetail, Integer> dcQty = new TableColumn<>("Số lượng");
            TableColumn<ExportDetail, BigDecimal> dcPrice = new TableColumn<>("Giá xuất");
            dcProd.setCellValueFactory(new PropertyValueFactory<>("productName"));
            dcQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            dcPrice.setCellValueFactory(new PropertyValueFactory<>("exportPrice"));
            dcPrice.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(BigDecimal v, boolean empty) {
                    super.updateItem(v, empty);
                    setText(empty || v == null ? null : currFmt.format(v) + " ₫");
                }
            });
            detailTable.getColumns().addAll(dcProd, dcQty, dcPrice);
            detailTable.setPrefHeight(180);
            ObservableList<ExportDetail> detailList = FXCollections.observableArrayList();
            detailTable.setItems(detailList);

            HBox addRow = new HBox(8);
            ComboBox<Product> productBox = new ComboBox<>(FXCollections.observableArrayList(products));
            productBox.setPromptText("Chọn sản phẩm");
            productBox.setMaxWidth(Double.MAX_VALUE);
            TextField qtyField = new TextField();
            qtyField.setPromptText("Số lượng");
            qtyField.setPrefWidth(80);
            TextField priceField = new TextField();
            priceField.setPromptText("Giá xuất");
            priceField.setPrefWidth(120);
            Button addBtn = new Button("Thêm dòng");
            addBtn.setOnAction(e -> {
                Product p = productBox.getValue();
                if (p == null || qtyField.getText().isBlank() || priceField.getText().isBlank()) return;
                ExportDetail d = new ExportDetail();
                d.setProductId(p.getProductId());
                d.setProductName(p.getProductName());
                d.setQuantity(Integer.parseInt(qtyField.getText().trim()));
                d.setExportPrice(new BigDecimal(priceField.getText().trim()));
                detailList.add(d);
                productBox.setValue(null);
                qtyField.clear();
                priceField.clear();
            });
            HBox.setHgrow(productBox, Priority.ALWAYS);
            addRow.getChildren().addAll(productBox, qtyField, priceField, addBtn);

            root.getChildren().addAll(new Label("Chi tiết xuất:"), detailTable, addRow);
            dialog.getDialogPane().setContent(root);

            dialog.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.OK) {
                    if (detailList.isEmpty()) {
                        showWarning("Vui lòng thêm ít nhất 1 sản phẩm.");
                        return;
                    }
                    try {
                        ExportTicket ticket = new ExportTicket();
                        ticket.setAccountId(SessionManager.getInstance().getCurrentAccount().getAccountId());
                        BigDecimal total = detailList.stream()
                                .map(d -> d.getExportPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        ticket.setTotalAmount(total);
                        ticket.setCreateAt(LocalDateTime.now());
                        exportBus.create(ticket, new ArrayList<>(detailList));
                        loadData();
                    } catch (Exception ex) {
                        showError("Lỗi tạo phiếu: " + ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showError("Lỗi mở form: " + e.getMessage());
        }
    }

    private void showDetails(ExportTicket ticket) {
        try {
            List<ExportDetail> details = exportBus.getDetails(ticket.getExportId());
            Dialog<ButtonType> d = new Dialog<>();
            d.setTitle("Chi tiết phiếu xuất #" + ticket.getExportId());
            d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            TableView<ExportDetail> tv = new TableView<>();
            TableColumn<ExportDetail, String> c1 = new TableColumn<>("Sản phẩm");
            TableColumn<ExportDetail, Integer> c2 = new TableColumn<>("Số lượng");
            TableColumn<ExportDetail, BigDecimal> c3 = new TableColumn<>("Giá xuất");
            c1.setCellValueFactory(new PropertyValueFactory<>("productName"));
            c2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            c3.setCellValueFactory(new PropertyValueFactory<>("exportPrice"));
            c3.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(BigDecimal v, boolean empty) {
                    super.updateItem(v, empty);
                    setText(empty || v == null ? null : currFmt.format(v) + " ₫");
                }
            });
            tv.getColumns().addAll(c1, c2, c3);
            tv.setPrefWidth(450);
            tv.setPrefHeight(300);
            if (details != null) tv.setItems(FXCollections.observableArrayList(details));
            d.getDialogPane().setContent(tv);
            d.showAndWait();
        } catch (Exception e) {
            showError("Lỗi tải chi tiết: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void showWarning(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

