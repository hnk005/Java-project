package com.example.quanlykho.controller;

import com.example.quanlykho.bus.ImportTicketBus;
import com.example.quanlykho.bus.ProductBus;
import com.example.quanlykho.bus.SupplierBus;
import com.example.quanlykho.model.product.Product;
import com.example.quanlykho.model.supplier.Supplier;
import com.example.quanlykho.model.ticket.ImportDetail;
import com.example.quanlykho.model.ticket.ImportTicket;
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

public class ImportController implements Initializable {

    @FXML
    private TableView<ImportTicket> importTable;
    @FXML
    private TableColumn<ImportTicket, Integer> colId;
    @FXML
    private TableColumn<ImportTicket, String> colSupplier;
    @FXML
    private TableColumn<ImportTicket, String> colAccount;
    @FXML
    private TableColumn<ImportTicket, BigDecimal> colTotal;
    @FXML
    private TableColumn<ImportTicket, String> colDate;

    private final ImportTicketBus importBus = new ImportTicketBus();
    private final SupplierBus supplierBus = new SupplierBus();
    private final ProductBus productBus = new ProductBus();
    private final NumberFormat currFmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    private final DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("importId"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
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

        // Row double-click to view details
        importTable.setRowFactory(tv -> {
            TableRow<ImportTicket> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) showDetails(row.getItem());
            });
            return row;
        });
    }

    private void loadData() {
        try {
            List<ImportTicket> list = importBus.getAll();
            importTable.setItems(list != null ? FXCollections.observableArrayList(list) : FXCollections.observableArrayList());
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
            List<Supplier> suppliers = supplierBus.getAll();
            List<Product> products = productBus.getAll();
            if (suppliers == null || suppliers.isEmpty()) {
                showWarning("Chưa có nhà cung cấp.");
                return;
            }
            if (products == null || products.isEmpty()) {
                showWarning("Chưa có sản phẩm.");
                return;
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Tạo phiếu nhập kho");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().setPrefWidth(600);

            VBox root = new VBox(10);
            root.setPadding(new Insets(15));

            ComboBox<Supplier> supplierBox = new ComboBox<>(FXCollections.observableArrayList(suppliers));
            supplierBox.setMaxWidth(Double.MAX_VALUE);
            supplierBox.setPromptText("Chọn nhà cung cấp");

            // Detail table inside dialog
            TableView<ImportDetail> detailTable = new TableView<>();
            TableColumn<ImportDetail, String> dcProd = new TableColumn<>("Sản phẩm");
            TableColumn<ImportDetail, Integer> dcQty = new TableColumn<>("Số lượng");
            TableColumn<ImportDetail, BigDecimal> dcPrice = new TableColumn<>("Giá nhập");
            dcProd.setCellValueFactory(new PropertyValueFactory<>("productName"));
            dcQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            dcPrice.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
            dcPrice.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(BigDecimal v, boolean empty) {
                    super.updateItem(v, empty);
                    setText(empty || v == null ? null : currFmt.format(v) + " ₫");
                }
            });
            detailTable.getColumns().addAll(dcProd, dcQty, dcPrice);
            detailTable.setPrefHeight(180);
            ObservableList<ImportDetail> detailList = FXCollections.observableArrayList();
            detailTable.setItems(detailList);

            // Add detail row
            HBox addRow = new HBox(8);
            ComboBox<Product> productBox = new ComboBox<>(FXCollections.observableArrayList(products));
            productBox.setPromptText("Chọn sản phẩm");
            productBox.setMaxWidth(Double.MAX_VALUE);
            TextField qtyField = new TextField();
            qtyField.setPromptText("Số lượng");
            qtyField.setPrefWidth(80);
            TextField priceField = new TextField();
            priceField.setPromptText("Giá nhập");
            priceField.setPrefWidth(120);
            Button addBtn = new Button("Thêm dòng");
            addBtn.setOnAction(e -> {
                Product p = productBox.getValue();
                if (p == null || qtyField.getText().isBlank() || priceField.getText().isBlank()) return;
                ImportDetail d = new ImportDetail();
                d.setProductId(p.getProductId());
                d.setProductName(p.getProductName());
                d.setQuantity(Integer.parseInt(qtyField.getText().trim()));
                d.setImportPrice(new BigDecimal(priceField.getText().trim()));
                detailList.add(d);
                productBox.setValue(null);
                qtyField.clear();
                priceField.clear();
            });
            HBox.setHgrow(productBox, Priority.ALWAYS);
            addRow.getChildren().addAll(productBox, qtyField, priceField, addBtn);

            root.getChildren().addAll(
                    new Label("Nhà cung cấp:"), supplierBox,
                    new Label("Chi tiết nhập:"), detailTable, addRow
            );
            dialog.getDialogPane().setContent(root);

            dialog.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.OK) {
                    Supplier sup = supplierBox.getValue();
                    if (sup == null) {
                        showWarning("Vui lòng chọn NCC.");
                        return;
                    }
                    if (detailList.isEmpty()) {
                        showWarning("Vui lòng thêm ít nhất 1 sản phẩm.");
                        return;
                    }
                    try {
                        ImportTicket ticket = new ImportTicket();
                        ticket.setSupplierId(sup.getSupplierId());
                        ticket.setAccountId(SessionManager.getInstance().getCurrentAccount().getAccountId());
                        BigDecimal total = detailList.stream()
                                .map(d -> d.getImportPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        ticket.setTotalAmount(total);
                        ticket.setCreateAt(LocalDateTime.now());
                        importBus.create(ticket, new ArrayList<>(detailList));
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

    private void showDetails(ImportTicket ticket) {
        try {
            List<ImportDetail> details = importBus.getDetails(ticket.getImportId());
            Dialog<ButtonType> d = new Dialog<>();
            d.setTitle("Chi tiết phiếu nhập #" + ticket.getImportId());
            d.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            TableView<ImportDetail> tv = new TableView<>();
            TableColumn<ImportDetail, String> c1 = new TableColumn<>("Sản phẩm");
            TableColumn<ImportDetail, Integer> c2 = new TableColumn<>("Số lượng");
            TableColumn<ImportDetail, BigDecimal> c3 = new TableColumn<>("Giá nhập");
            c1.setCellValueFactory(new PropertyValueFactory<>("productName"));
            c2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            c3.setCellValueFactory(new PropertyValueFactory<>("importPrice"));
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

