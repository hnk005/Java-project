package com.example.quanlykho.controller;

import com.example.quanlykho.bus.SupplierBus;
import com.example.quanlykho.model.supplier.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, Integer> colId;
    @FXML
    private TableColumn<Supplier, String> colName;
    @FXML
    private TableColumn<Supplier, String> colPhone;
    @FXML
    private TableColumn<Supplier, String> colAddress;
    @FXML
    private TextField searchField;

    private final SupplierBus supplierBus = new SupplierBus();
    private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        loadData();
        searchField.textProperty().addListener((obs, old, nv) -> {
            FilteredList<Supplier> filtered = new FilteredList<>(supplierList, s ->
                    nv == null || nv.isEmpty() || s.getSupplierName().toLowerCase().contains(nv.toLowerCase())
                            || s.getPhone().contains(nv)
            );
            supplierTable.setItems(filtered);
        });
    }

    private void loadData() {
        try {
            List<Supplier> list = supplierBus.getAll();
            supplierList = list != null ? FXCollections.observableArrayList(list) : FXCollections.observableArrayList();
            supplierTable.setItems(supplierList);
        } catch (Exception e) {
            showError("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void onAdd() {
        showFormDialog(null);
    }

    @FXML
    private void onRefresh() {
        loadData();
    }

    @FXML
    private void onEdit() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhà cung cấp cần sửa.");
            return;
        }
        showFormDialog(selected);
    }

    @FXML
    private void onDelete() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn nhà cung cấp cần xóa.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xóa NCC \"" + selected.getSupplierName() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    supplierBus.delete(selected.getSupplierId());
                    loadData();
                } catch (Exception e) {
                    showError("Lỗi xóa: " + e.getMessage());
                }
            }
        });
    }

    private void showFormDialog(Supplier supplier) {
        boolean isEdit = supplier != null;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Sửa nhà cung cấp" : "Thêm nhà cung cấp");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField(isEdit ? supplier.getSupplierName() : "");
        TextField phoneField = new TextField(isEdit ? supplier.getPhone() : "");
        TextField addressField = new TextField(isEdit ? supplier.getAddress() : "");
        addressField.setPrefWidth(250);

        grid.add(new Label("Tên NCC:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Số điện thoại:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Địa chỉ:"), 0, 2);
        grid.add(addressField, 1, 2);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Supplier s = isEdit ? supplier : new Supplier();
                    s.setSupplierName(nameField.getText().trim());
                    s.setPhone(phoneField.getText().trim());
                    s.setAddress(addressField.getText().trim());
                    if (isEdit) supplierBus.update(s);
                    else supplierBus.create(s);
                    loadData();
                } catch (Exception e) {
                    showError("Lỗi lưu: " + e.getMessage());
                }
            }
        });
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

