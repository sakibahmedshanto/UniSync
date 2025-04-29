package com.example.UniSync;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddBookController implements Initializable {

    @FXML
    private TextField book_title;
    @FXML
    private TextField book_id;

    @FXML
    private TextField book_author;

    @FXML
    private TextField book_isbn;

    @FXML
    private ComboBox<String> book_category;

    @FXML
    private TextField book_quantity;

    @FXML
    private ComboBox<String> book_status;

    @FXML
    private Button book_importBtn;

    @FXML
    private Button book_addBtn;
    @FXML
    private Button book_updateBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private AlertMessage alert = new AlertMessage();


    public void libraryAddBtn() {
        clearTempData();
        if (book_title.getText().isEmpty()
                || book_author.getText().isEmpty()
                || book_isbn.getText().isEmpty()
                || book_category.getSelectionModel().getSelectedItem() == null
                || book_quantity.getText().isEmpty()
                || book_status.getSelectionModel().getSelectedItem() == null) {
            alert.errorMessage("Please fill all blank fields.");
            return;
        }

        connect = Database.connectDB();

        try {
            String callProcedure = "{CALL AddBook(?, ?, ?, ?, ?, ?)}"; //procedures
            CallableStatement callableStatement = connect.prepareCall(callProcedure);
            callableStatement.setString(1, book_title.getText());
            callableStatement.setString(2, book_author.getText());
            callableStatement.setString(3, book_category.getSelectionModel().getSelectedItem());
            callableStatement.setString(4, book_isbn.getText());
            callableStatement.setInt(5, Integer.parseInt(book_quantity.getText()));
            callableStatement.setString(6, book_status.getSelectionModel().getSelectedItem());

            callableStatement.executeUpdate();

            alert.successMessage("Book added successfully using the stored procedure!");
            clearFields();
            clearTempData();
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Error adding book: " + e.getMessage());
        }
    }

    public void updateBtn() {
        Platform.runLater(() -> book_addBtn.setVisible(false));
        int selectedBookID = ListData.temp_bookID;
        if (book_title.getText().isEmpty()
                || book_author.getText().isEmpty()
                || book_isbn.getText().isEmpty()
                || book_category.getSelectionModel().getSelectedItem() == null
                || book_quantity.getText().isEmpty()
                || book_status.getSelectionModel().getSelectedItem() == null) {
            alert.errorMessage("Please fill all blank fields.");
            return;
        }

        connect = Database.connectDB();

        try {
            String callProcedure = "{CALL UpdateBook(?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement callableStatement = connect.prepareCall(callProcedure);
            callableStatement.setInt(1, selectedBookID);
            callableStatement.setString(2, book_title.getText());
            callableStatement.setString(3, book_author.getText());
            callableStatement.setString(4, book_category.getSelectionModel().getSelectedItem());
            callableStatement.setString(5, book_isbn.getText());
            callableStatement.setInt(6, Integer.parseInt(book_quantity.getText()));
            callableStatement.setString(7, book_status.getSelectionModel().getSelectedItem());

            callableStatement.executeUpdate();

            alert.successMessage("Book updated successfully using the stored procedure!");
            clearFields();
            clearTempData();
        } catch (Exception e) {
            e.printStackTrace();
            alert.errorMessage("Error updating book: " + e.getMessage());
        }

    }
    public void clearFields() {
        book_title.clear();
        book_author.clear();
        book_isbn.clear();
        book_quantity.clear();
        book_category.getSelectionModel().clearSelection();
        book_status.getSelectionModel().clearSelection();
    }
    public void categoryList() {
        List<String> categories = List.of("Fiction", "Non-Fiction", "Science", "History", "Biography");
        ObservableList<String> categoryData = FXCollections.observableArrayList(categories);
        book_category.setItems(categoryData);
    }
    public void statusList() {
        List<String> statuses = List.of("Available", "Checked Out", "Reserved");
        ObservableList<String> statusData = FXCollections.observableArrayList(statuses);
        book_status.setItems(statusData);
    }
    private void clearTempData() {
        ListData.temp_bookID = null;
        ListData.temp_title = null;
        ListData.temp_author = null;
        ListData.temp_category = null;
        ListData.temp_isbn = null;
        ListData.temp_quantity = null;
        ListData.temp_status = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryList();
        statusList();


        if (ListData.temp_bookID != null) {
            //updating
            book_title.setText(ListData.temp_title);
            book_author.setText(ListData.temp_author);
            book_isbn.setText(ListData.temp_isbn);
            book_category.getSelectionModel().select(ListData.temp_category);
            book_quantity.setText(String.valueOf(ListData.temp_quantity));
            book_status.getSelectionModel().select(ListData.temp_status);


            book_addBtn.setVisible(false);
        } else {
            // show add button
            book_addBtn.setVisible(true);
            book_updateBtn.setVisible(false);
        }
    }

}
