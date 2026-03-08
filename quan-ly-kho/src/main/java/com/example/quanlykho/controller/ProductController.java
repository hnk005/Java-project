package com.example.quanlykho.controller;

import com.example.quanlykho.bus.CategoryBus;
import com.example.quanlykho.bus.ProductBus;
import com.example.quanlykho.model.category.Category;
import com.example.quanlykho.model.product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> colId;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colCategory;
    @FXML
    private TableColumn<Product, Integer> colQuantity;
    @FXML
    private TableColumn<Product, BigDecimal> colPrice;
    @FXML
    private TableColumn<Product, String> colImage;
    @FXML
    private TextField searchField;

    private final ProductBus productBus = new ProductBus();
    private final CategoryBus categoryBus = new CategoryBus();
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadData();
        setupSearch();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        // Format price column
        colPrice.setCellFactory(col -> new TableCell<>() {
            final NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : fmt.format(price) + " ₫");
            }
        });

        // Quantity color indicator
        colQuantity.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer qty, boolean empty) {
                super.updateItem(qty, empty);
                if (empty || qty == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(qty.toString());
                    setStyle(qty <= 10 ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;" : "");
                }
            }
        });
    }

    private void loadData() {
        try {
            List<Product> list = productBus.getAll();
            productList = list != null ? FXCollections.observableArrayList(list) : FXCollections.observableArrayList();
            productTable.setItems(productList);
        } catch (Exception e) {
            showError("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            FilteredList<Product> filtered = new FilteredList<>(productList, p ->
                    newVal == null || newVal.isEmpty() ||
                            p.getProductName().toLowerCase().contains(newVal.toLowerCase())
            );
            productTable.setItems(filtered);
        });
    }

    @FXML
    private void onAdd() {
        showFormDialog(null);
    }

    @FXML
    private void onEdit() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn sản phẩm cần sửa.");
            return;
        }
        showFormDialog(selected);
    }

    @FXML
    private void onDelete() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn sản phẩm cần xóa.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa sản phẩm \"" + selected.getProductName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    productBus.delete(selected.getProductId());
                    loadData();
                } catch (Exception e) {
                    showError("Lỗi xóa sản phẩm: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onRefresh() {
        loadData();
    }

    private void showFormDialog(Product product) {
        try {
            List<Category> categories = categoryBus.getAll();
            boolean isEdit = product != null;
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(isEdit ? "Sửa sản phẩm" : "Thêm sản phẩm");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField nameField = new TextField(isEdit ? product.getProductName() : "");
            nameField.setPromptText("Tên sản phẩm");

            ComboBox<Category> categoryBox = new ComboBox<>();
            if (categories != null) categoryBox.setItems(FXCollections.observableArrayList(categories));
            if (isEdit && categories != null) {
                categories.stream().filter(c -> c.getCategoryId() == product.getCategoryId())
                        .findFirst().ifPresent(categoryBox::setValue);
            }

            TextField quantityField = new TextField(isEdit ? String.valueOf(product.getQuantity()) : "0");
            TextField priceField = new TextField(isEdit && product.getPrice() != null ? product.getPrice().toPlainString() : "0");
            TextField imageField = new TextField(isEdit ? product.getImageUrl() : "");
            imageField.setPromptText("Tên file ảnh");

            grid.add(new Label("Tên sản phẩm:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Danh mục:"), 0, 1);
            grid.add(categoryBox, 1, 1);
            grid.add(new Label("Số lượng:"), 0, 2);
            grid.add(quantityField, 1, 2);
            grid.add(new Label("Giá bán (₫):"), 0, 3);
            grid.add(priceField, 1, 3);
            grid.add(new Label("Ảnh:"), 0, 4);
            grid.add(imageField, 1, 4);

            dialog.getDialogPane().setContent(grid);
            dialog.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.OK) {
                    try {
                        Product p = isEdit ? product : new Product();
                        p.setProductName(nameField.getText().trim());
                        Category cat = categoryBox.getValue();
                        p.setCategoryId(cat != null ? cat.getCategoryId() : 0);
                        p.setQuantity(Integer.parseInt(quantityField.getText().trim()));
                        p.setPrice(new BigDecimal(priceField.getText().trim()));
                        p.setImageUrl(imageField.getText().trim());
                        if (isEdit) productBus.update(p);
                        else productBus.create(p);
                        loadData();
                    } catch (Exception e) {
                        showError("Lỗi lưu dữ liệu: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showError("Lỗi mở form: " + e.getMessage());
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

