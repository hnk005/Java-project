package com.example.quanlykho.controller;

import com.example.quanlykho.bus.CategoryBus;
import com.example.quanlykho.model.category.Category;
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

public class CategoryController implements Initializable {

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, Integer> colId;
    @FXML
    private TableColumn<Category, String> colName;
    @FXML
    private TableColumn<Category, String> colDescription;
    @FXML
    private TextField searchField;

    private final CategoryBus categoryBus = new CategoryBus();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        loadData();
        searchField.textProperty().addListener((obs, old, nv) -> {
            FilteredList<Category> filtered = new FilteredList<>(categoryList, c ->
                    nv == null || nv.isEmpty() || c.getCategoryName().toLowerCase().contains(nv.toLowerCase())
            );
            categoryTable.setItems(filtered);
        });
    }

    private void loadData() {
        try {
            List<Category> list = categoryBus.getAll();
            categoryList = list != null ? FXCollections.observableArrayList(list) : FXCollections.observableArrayList();
            categoryTable.setItems(categoryList);
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
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn danh mục cần sửa.");
            return;
        }
        showFormDialog(selected);
    }

    @FXML
    private void onDelete() {
        Category selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn danh mục cần xóa.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Xóa danh mục \"" + selected.getCategoryName() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    categoryBus.delete(selected.getCategoryId());
                    loadData();
                } catch (Exception e) {
                    showError("Lỗi xóa: " + e.getMessage());
                }
            }
        });
    }

    private void showFormDialog(Category category) {
        boolean isEdit = category != null;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Sửa danh mục" : "Thêm danh mục");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField(isEdit ? category.getCategoryName() : "");
        nameField.setPromptText("Tên danh mục");
        TextField descField = new TextField(isEdit ? category.getDescription() : "");
        descField.setPromptText("Mô tả");

        grid.add(new Label("Tên danh mục:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Mô tả:"), 0, 1);
        grid.add(descField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Category c = isEdit ? category : new Category();
                    c.setCategoryName(nameField.getText().trim());
                    c.setDescription(descField.getText().trim());
                    if (isEdit) categoryBus.update(c);
                    else categoryBus.create(c);
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

