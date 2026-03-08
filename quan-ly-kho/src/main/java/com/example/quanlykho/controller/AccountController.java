package com.example.quanlykho.controller;

import com.example.quanlykho.bus.AccountBus;
import com.example.quanlykho.model.account.Account;
import com.example.quanlykho.model.account.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, Integer> colId;
    @FXML
    private TableColumn<Account, String> colUsername;
    @FXML
    private TableColumn<Account, String> colFullName;
    @FXML
    private TableColumn<Account, String> colRole;
    @FXML
    private TableColumn<Account, Boolean> colStatus;
    @FXML
    private TextField searchField;
    @FXML
    private Button statusToggleBtn;
    @FXML
    private FontIcon statusIcon;

    private final AccountBus accountBus = new AccountBus();
    private ObservableList<Account> accountList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colRole.setCellValueFactory(data -> {
            Role r = data.getValue().getRole();
            return new javafx.beans.property.SimpleStringProperty(r != null ? (r == Role.ADMIN ? "Quản trị viên" : "Nhân viên") : "");
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusIcon.setIconLiteral("mdi-lock");
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(v ? "Hoạt động" : "Khóa");
                    setStyle(v ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
                }
            }
        });
        statusToggleBtn.setText("Khóa tài khoản");
        accountTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Boolean status = newValue.isStatus();
                statusIcon.setIconLiteral(status ? "mdi-lock" : "mdi-lock-open");
                statusToggleBtn.setText(status ? "Khóa tài khoản" : "Mở khóa tài khoản");
            }
        });
        loadData();
        searchField.textProperty().addListener((obs, old, nv) -> {
            FilteredList<Account> filtered = new FilteredList<>(accountList, a ->
                    nv == null || nv.isEmpty() ||
                            a.getUsername().toLowerCase().contains(nv.toLowerCase()) ||
                            a.getFullName().toLowerCase().contains(nv.toLowerCase())
            );
            accountTable.setItems(filtered);
        });
    }

    private void loadData() {
        try {
            List<Account> list = accountBus.getAll();
            accountList = list != null ? FXCollections.observableArrayList(list) : FXCollections.observableArrayList();
            accountTable.setItems(accountList);
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
        Account selected = accountTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn tài khoản cần sửa.");
            return;
        }
        showFormDialog(selected);
    }

    @FXML
    private void onToggleStatus() {
        Account selected = accountTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Vui lòng chọn tài khoản.");
            return;
        }
        try {
            accountBus.toggleStatus(selected.getAccountId(), !selected.isStatus());
            loadData();
        } catch (Exception e) {
            showError("Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }

    private void showFormDialog(Account account) {
        boolean isEdit = account != null;
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(isEdit ? "Sửa tài khoản" : "Thêm tài khoản");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField usernameField = new TextField(isEdit ? account.getUsername() : "");
        usernameField.setDisable(isEdit);
        TextField fullNameField = new TextField(isEdit ? account.getFullName() : "");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(isEdit ? "Để trống nếu không đổi" : "Mật khẩu");
        ComboBox<Role> roleBox = new ComboBox<>(FXCollections.observableArrayList(Role.values()));
        roleBox.setValue(isEdit ? account.getRole() : Role.STAFF);
        CheckBox statusBox = new CheckBox("Hoạt động");
        statusBox.setSelected(isEdit ? account.isStatus() : true);

        grid.add(new Label("Tên đăng nhập:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Họ và tên:"), 0, 1);
        grid.add(fullNameField, 1, 1);
        grid.add(new Label("Mật khẩu:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Vai trò:"), 0, 3);
        grid.add(roleBox, 1, 3);
        grid.add(new Label("Trạng thái:"), 0, 4);
        grid.add(statusBox, 1, 4);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Account a = isEdit ? account : new Account();
                    a.setUsername(usernameField.getText().trim());
                    a.setFullName(fullNameField.getText().trim());
                    a.setRole(roleBox.getValue());
                    a.setStatus(statusBox.isSelected());
                    String pwd = passwordField.getText().trim();
                    if (!isEdit) {
                        a.setPassword(pwd);
                        accountBus.create(a);
                    } else {
                        accountBus.update(a);
                        if (!pwd.isEmpty()) accountBus.updatePassword(a.getAccountId(), pwd);
                    }
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

