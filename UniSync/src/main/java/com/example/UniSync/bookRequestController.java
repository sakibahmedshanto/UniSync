package com.example.UniSync;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class bookRequestController implements Initializable {

    @FXML
    private DatePicker duedate1;

    @FXML
    private ComboBox<String> book_status;

    @FXML
    private TextField if_rejected;

    @FXML
    private Button submit;

    @FXML
    private Button cancel;

    private int requestId = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Pending", "Approved", "Rejected");
        book_status.setItems(statusOptions);
        if_rejected.setDisable(true);


        book_status.setOnAction(event -> handleStatusChange());

        System.out.println("Controller initialized: Ready to handle book requests.");
    }


    public void loadRequestData(int requestId, String dueDate, String status, String rejectedReason) {
        this.requestId = requestId;
        System.out.println("Loaded request ID: " + this.requestId);
        System.out.println("Raw due date from database: " + dueDate);

        if (duedate1 == null) {
            System.out.println("Error: DatePicker (duedate1) is null! Check FXML file or controller loading.");
            return;
        }


        if (dueDate != null && !dueDate.trim().isEmpty() && !"null".equalsIgnoreCase(dueDate)) {
            try {
                LocalDate parsedDate = LocalDate.parse(dueDate);
                duedate1.setValue(parsedDate);
                System.out.println("Due date set to: " + parsedDate);
            } catch (Exception e) {
                System.out.println("Error parsing due date: " + e.getMessage());
                duedate1.setValue(null);
            }
        } else {
            System.out.println("Due date is null or empty, setting to null");
            duedate1.setValue(null);
        }


        if (status != null && !status.isEmpty()) {
            book_status.setValue(status);
        }


        if ("Rejected".equals(status)) {
            if_rejected.setDisable(false);
            duedate1.setDisable(true);
            if (rejectedReason != null) {
                if_rejected.setText(rejectedReason);
            }
        } else {
            if_rejected.setDisable(true);
            duedate1.setDisable(false);
            if_rejected.clear();
        }
    }



    /**
     * Enables or disables the "If Rejected" field based on status selection.
     */
    private void handleStatusChange() {
        String selectedStatus = book_status.getValue();

        if ("Rejected".equals(selectedStatus)) {
            if_rejected.setDisable(false);
            duedate1.setDisable(true);
        } else {
            if_rejected.setDisable(true);
            if_rejected.clear();
            duedate1.setDisable(false);
        }
    }



    @FXML
    private void handleSubmit(ActionEvent event) {
        System.out.println("Submitting request with ID: " + requestId);

        if (requestId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "No request selected.");
            return;
        }

        // **✅ Fix: Due date should only be required if status is NOT "Rejected"**
        if (!"Rejected".equals(book_status.getValue()) && duedate1.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Due Date", "Please select a due date.");
            return;
        }

        if (book_status.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Status", "Please select a status.");
            return;
        }

        updateRequestInDatabase();
    }



    /* private void updateRequestInDatabase() {
         Connection conn = Database.connectDB();
         String query = "UPDATE book_requests SET due_date = ?, status = ?, if_rejected = ? WHERE request_id = ?";
         String reduceQuantityQuery = "UPDATE books SET quantity = quantity - 1 WHERE isbn = (SELECT isbn FROM book_requests WHERE request_id = ?) AND quantity > 0";

         try (PreparedStatement stmt = conn.prepareStatement(query);
              PreparedStatement reduceStmt = conn.prepareStatement(reduceQuantityQuery)) {

             stmt.setDate(1, Date.valueOf(duedate1.getValue()));
             stmt.setString(2, book_status.getValue());

             if ("Rejected".equals(book_status.getValue())) {
                 stmt.setString(3, if_rejected.getText());
             } else {
                 stmt.setString(3, null);
             }

             stmt.setInt(4, requestId);

             int rowsUpdated = stmt.executeUpdate();
             if (rowsUpdated > 0) {
                 // Only reduce book quantity if the request is "Approved"
                 if ("Approved".equals(book_status.getValue())) {
                     reduceStmt.setInt(1, requestId);
                     int rowsReduced = reduceStmt.executeUpdate();
                     if (rowsReduced > 0) {
                         System.out.println("Book quantity reduced successfully.");
                     } else {
                         System.out.println("Book quantity not reduced. Book might be out of stock.");
                     }
                 }

                 showAlert(Alert.AlertType.INFORMATION, "Success", "Request updated successfully!");
             } else {
                 showAlert(Alert.AlertType.WARNING, "Warning", "No records were updated. Please check the request ID.");
             }

             closeWindow();
         } catch (SQLException e) {
             e.printStackTrace();
             showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update request.");
         }
     }*/
    private void updateRequestInDatabase() {
        Connection conn = Database.connectDB();
        String query = "UPDATE book_requests SET due_date = ?, status = ?, if_rejected = ? WHERE request_id = ?";
        String reduceQuantityQuery = "UPDATE books SET quantity = quantity - 1 WHERE isbn = (SELECT isbn FROM book_requests WHERE request_id = ?) AND quantity > 0";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             PreparedStatement reduceStmt = conn.prepareStatement(reduceQuantityQuery)) {

            // ✅ FIX: Only set due_date if it's not null
            if (duedate1.getValue() != null) {
                stmt.setDate(1, Date.valueOf(duedate1.getValue()));
            } else {
                stmt.setNull(1, java.sql.Types.DATE);
            }

            stmt.setString(2, book_status.getValue());

            if ("Rejected".equals(book_status.getValue())) {
                stmt.setString(3, if_rejected.getText());
            } else {
                stmt.setString(3, null);
            }

            stmt.setInt(4, requestId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Only reduce book quantity if the request is "Approved"
                if ("Approved".equals(book_status.getValue())) {
                    reduceStmt.setInt(1, requestId);
                    int rowsReduced = reduceStmt.executeUpdate();
                    if (rowsReduced > 0) {
                        System.out.println("Book quantity reduced successfully.");
                    } else {
                        System.out.println("Book quantity not reduced. Book might be out of stock.");
                    }
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Request updated successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "No records were updated. Please check the request ID.");
            }

            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update request.");
        }
    }




    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
