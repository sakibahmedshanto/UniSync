/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.UniSync;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author WINDOWS 10
 */
public class StudentMainFormController implements Initializable {

    @FXML
    private Label student_id;

    @FXML
    private Button studentInformation_btn;
    @FXML
    private Button bookRequests_btn;

    @FXML
    private Button tracker;

    @FXML
    private Button logout_btn;

    @FXML
    private TableView<DataStudentHandle> table_view;

    @FXML
    private TableColumn<DataStudentHandle, String> studentInfo_col_teacherID;

    @FXML
    private TableColumn<DataStudentHandle, String> studentInfo_col_name;

    @FXML
    private TableColumn<DataStudentHandle, String> studentInfo_col_gender;

    @FXML
    private TableColumn<DataStudentHandle, String> studentInfo_col_YE;

    @FXML
    private Circle circle_image;

    @FXML
    private Label teacher_id;

    @FXML
    private Label teacher_name;

    @FXML
    private Label teacher_gender;

    @FXML
    private Label teacher_date;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;


    AlertMessage alert = new AlertMessage();

    public ObservableList<DataStudentHandle> teacherSetData() {
        ObservableList<DataStudentHandle> listData = FXCollections.observableArrayList();
        String sql = "SELECT ts.teacher_id, t.full_name AS teacher_name, t.gender AS teacher_gender, ts.date_insert " +
                "FROM teacher_student ts " +
                "JOIN teacher t ON ts.teacher_id = t.teacher_id " +
                "WHERE ts.stud_studentID = '" + student_id.getText() + "' AND ts.date_delete IS NULL";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (!result.isBeforeFirst()) {
                System.out.println("No teacher data found for student ID: " + student_id.getText());
            }

            while (result.next()) {
                DataStudentHandle dsh = new DataStudentHandle(
                        result.getString("teacher_id"),
                        student_id.getText(), // student's ID
                        result.getString("teacher_name"), // teacher's name
                        result.getString("teacher_gender"), // teacher's gender
                        result.getDate("date_insert")
                );
                listData.add(dsh);
            }

            System.out.println("Number of teachers displayed: " + listData.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<DataStudentHandle> teacherListData;

    public void teacherDisplayData() {
        teacherListData = teacherSetData();

        studentInfo_col_teacherID.setCellValueFactory(new PropertyValueFactory<>("teacherID"));
        studentInfo_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentInfo_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        studentInfo_col_YE.setCellValueFactory(new PropertyValueFactory<>("dateInsert"));

        table_view.setItems(teacherListData);

        System.out.println("Number of teachers displayed: " + teacherListData.size()); // Debug print
    }

    private Image image;

    public void teacherSelectData() {
        DataStudentHandle dsh = table_view.getSelectionModel().getSelectedItem();
        int num = table_view.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        String sql = "SELECT * FROM teacher WHERE teacher_id = '"
                + dsh.getTeacherID() + "'";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                String path = "File:" + result.getString("image");

                image = new Image(path, 164, 73, false, true);
                circle_image.setFill(new ImagePattern(image));

                teacher_id.setText(result.getString("teacher_id"));
                teacher_name.setText(result.getString("full_name"));
                teacher_gender.setText(result.getString("gender"));
                teacher_date.setText(result.getString("date_insert"));

                System.out.println("Teacher details updated for ID: " + dsh.getTeacherID()); // Debug print
            } else {
                System.out.println("No teacher found for ID: " + dsh.getTeacherID()); // Debug print
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public void studentIDDisplay() {
        String sql = "SELECT * FROM users WHERE username = '" + ListData.student_username + "'";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                String retrievedStudentID = result.getString("student_id");
                System.out.println("Found student_id for username " + ListData.student_username + ": " + retrievedStudentID);
                student_id.setText(retrievedStudentID);
            } else {
                System.out.println("No student found for username: " + ListData.student_username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    public void studentIDDisplay() {
        String temp_stuID = ListData.student_username; // Directly use the logged-in teacher's ID

        // Check if teacher ID is fetched
        if (temp_stuID == null || temp_stuID.isEmpty()) {
            System.out.println("Student username not set or empty in ListData."); // Debug statement
            return;
        }

        System.out.println("Student ID from login: " + temp_stuID); // Debug output to check the ID

        // Set the teacher ID label
        // student_id.setText(temp_stuID);

        String sql = "SELECT student_id FROM student WHERE student_id = '" + temp_stuID + "'";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                String temp_stuid = result.getString("student_id");
                student_id.setText("" + temp_stuid);
            } else {
                System.out.println("Student not found in database."); // Debug output
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logoutBtn() {

        try {
            if (alert.confirmMessage("Are you sure you want to logout?")) {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();

                logout_btn.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    private TableView<BookData> libraryTableView;
    @FXML
    private TableColumn<BookData, String> colIsbn, colTitle, colAuthor, colCategory, colStatus;

    public void loadBooks() {
        ObservableList<BookData> books = FXCollections.observableArrayList();

        // ✅ Updated query: Load only books where date_delete IS NULL
        String query = "SELECT title, author, category, isbn, status FROM books WHERE date_delete IS NULL";

        connect = Database.connectDB();
        try {
            prepare = connect.prepareStatement(query);
            result = prepare.executeQuery();

            while (result.next()) {
                books.add(new BookData(
                        null,
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("category"),
                        result.getString("isbn"),
                        null,
                        result.getString("status"),
                        null,
                        null,
                        null
                ));
            }

            // ✅ Set filtered books to TableView
            libraryTableView.setItems(books);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private AnchorPane library_form;

    @FXML
    private AnchorPane teacher_form1;

    @FXML
    private AnchorPane teacher_form2;
    @FXML
    private AnchorPane student_info_form;

    @FXML
    private AnchorPane tracker_form;

    @FXML
    private AnchorPane bookrequest_form;
    @FXML
    private Button studentlibrary_btn1;

    @FXML
    private Button  studentInfo_btn;


    public void switchForm(ActionEvent event) {
        library_form.setVisible(false);
        teacher_form1.setVisible(false);
        teacher_form2.setVisible(false);
        bookrequest_form.setVisible(false);
        student_info_form.setVisible(false);
        tracker_form.setVisible(false);
        addTask_form.setVisible(false);  // show form only when user clicks
        taskList_form.setVisible(false);

        if (event.getSource() == studentlibrary_btn1) {
            teacher_form1.setVisible(false);
            teacher_form2.setVisible(false);
            library_form.setVisible(true);
            bookrequest_form.setVisible(false);
            student_info_form.setVisible(false);
            tracker_form.setVisible(false);
            loadBooks();
            addTask_form.setVisible(false);  // show form only when user clicks
            taskList_form.setVisible(false);

        }

        else if (event.getSource() ==    studentInformation_btn) {
            library_form.setVisible(true);
            teacher_form1.setVisible(true);
            teacher_form2.setVisible(true);
            library_form.setVisible(false);
            bookrequest_form.setVisible(false);
            student_info_form.setVisible(false);
            tracker_form.setVisible(false);
            addTask_form.setVisible(false);  // show form only when user clicks
            taskList_form.setVisible(false);

        }

        else if (event.getSource() ==   bookRequests_btn) {
            library_form.setVisible(false);
            teacher_form1.setVisible(false);
            teacher_form2.setVisible(false);
            library_form.setVisible(false);
            bookrequest_form.setVisible(true);
            student_info_form.setVisible(false);
            tracker_form.setVisible(false);
            addTask_form.setVisible(false);  // show form only when user clicks
            taskList_form.setVisible(false);
            showApprovedBtn.setVisible(true);      // ✅ show it
            returnBookBtn.setVisible(false);

            handleRequest();

        }

        else if (event.getSource() ==     studentInfo_btn) {
            library_form.setVisible(true);
            teacher_form1.setVisible(true);
            teacher_form2.setVisible(true);
            library_form.setVisible(false);
            bookrequest_form.setVisible(false);
            student_info_form.setVisible(true);
            tracker_form.setVisible(false);
            showLoggedInStudentInfo();
            addTask_form.setVisible(false);  // show form only when user clicks
            taskList_form.setVisible(false);
        }

        else if (event.getSource() ==   tracker) {
            library_form.setVisible(false);
            teacher_form1.setVisible(false);
            teacher_form2.setVisible(false);
            library_form.setVisible(false);
            bookrequest_form.setVisible(false);
            student_info_form.setVisible(false);
            tracker_form.setVisible(true);
            addTask_form.setVisible(false);  // show form only when user clicks
            taskList_form.setVisible(false);

        }
    }
    private Map<BookData, Integer> bookIdMap = new HashMap<>();

    /*private void handleBookRequest() {
        BookData selectedBook = libraryTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            insertBookRequest(selectedBook);
        } else {
            System.out.println("No book selected!");
        }
    }*/

    @FXML
    private void handleBookRequest() {
        BookData selectedBook = libraryTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            insertBookRequest(selectedBook);
            handleRequest(); // Refresh the book requests table
        } else {
            alert.errorMessage("Please select a book first!");
        }
    }

   /* private void insertBookRequest(BookData book) {
        String query = "INSERT INTO book_requests (isbn, student_id, title, author, request_date, status) VALUES (?, ?, ?, ?, CURDATE(), 'Pending')";
        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getIsbn());  // Use ISBN to reference the book
            pstmt.setString(2, student_id.getText());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("Request submitted successfully.");
            } else {
                System.out.println("Failed to submit the request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error submitting book request: " + e.getMessage());
        }
    }*/
   /*private void insertBookRequest(BookData book) {
        // ✅ First, check if the student has already requested this book
        String checkQuery = "SELECT COUNT(*) FROM book_requests WHERE isbn = ? AND student_id = ? AND status = 'Pending'";

        // ✅ If not already requested, insert a new request
        String insertQuery = "INSERT INTO book_requests (isbn, student_id, title, author, request_date, status) VALUES (?, ?, ?, ?, CURDATE(), 'Pending')";

        try (Connection conn = Database.connectDB();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Check for an existing request
            checkStmt.setString(1, book.getIsbn());
            checkStmt.setString(2, student_id.getText());

            ResultSet rs = checkStmt.executeQuery();
            AlertMessage alert = new AlertMessage();
            // Check for an existing request
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Student has already requested this book.");

                alert.errorMessage("You have already requested this book.");
                return;
            }


            // Proceed to insert the request
            insertStmt.setString(1, book.getIsbn());
            insertStmt.setString(2, student_id.getText());
            insertStmt.setString(3, book.getTitle());
            insertStmt.setString(4, book.getAuthor());

            int result = insertStmt.executeUpdate();
            if (result > 0) {
                System.out.println("Request submitted successfully.");
                alert.successMessage("Your book request has been submitted.");
            } else {
                System.out.println("Failed to submit the request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Failed to submit your book request.");
        }
    }*/

    /* private void insertBookRequest(BookData book) {
         AlertMessage alert = new AlertMessage();

         String checkRequestQuery = "SELECT COUNT(*) FROM book_requests WHERE isbn = ? AND student_id = ? AND status = 'Pending'";
         String checkQuantityQuery = "SELECT quantity FROM books WHERE isbn = ? AND date_delete IS NULL";
         String insertQuery = "INSERT INTO book_requests (isbn, student_id, title, author, request_date, status) VALUES (?, ?, ?, ?, CURDATE(), 'Pending')";

         try (Connection conn = Database.connectDB();
              PreparedStatement checkRequestStmt = conn.prepareStatement(checkRequestQuery);
              PreparedStatement checkQtyStmt = conn.prepareStatement(checkQuantityQuery);
              PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

             // 🔍 1. Check if student has already requested this book
             checkRequestStmt.setString(1, book.getIsbn());
             checkRequestStmt.setString(2, student_id.getText());
             ResultSet rsRequest = checkRequestStmt.executeQuery();
             if (rsRequest.next() && rsRequest.getInt(1) > 0) {
                 alert.errorMessage("You have already requested this book.");
                 return;
             }

             // 📦 2. Check if quantity is 0
             checkQtyStmt.setString(1, book.getIsbn());
             ResultSet rsQty = checkQtyStmt.executeQuery();
             if (rsQty.next()) {
                 int quantity = rsQty.getInt("quantity");
                 if (quantity <= 0) {
                     alert.errorMessage("This book is currently out of stock.");
                     return;
                 }
             }

             // ✅ 3. If all good, insert the request
             insertStmt.setString(1, book.getIsbn());
             insertStmt.setString(2, student_id.getText());
             insertStmt.setString(3, book.getTitle());
             insertStmt.setString(4, book.getAuthor());

             int result = insertStmt.executeUpdate();
             if (result > 0) {
                 alert.successMessage("Your book request has been submitted.");
             } else {
                 alert.errorMessage("Failed to submit your book request.");
             }

         } catch (SQLException e) {
             e.printStackTrace();
             alert.errorMessage("Database error occurred.");
         }
     }*/
//27 march 2024
    private void insertBookRequest(BookData book) {
        AlertMessage alert = new AlertMessage();

        String checkRequestQuery = "SELECT COUNT(*) FROM book_requests WHERE isbn = ? AND student_id = ? AND status = 'Pending'";
        String insertQuery = "INSERT INTO book_requests (isbn, student_id, title, author, request_date, status) VALUES (?, ?, ?, ?, CURDATE(), 'Pending')";

        try (Connection conn = Database.connectDB();
             PreparedStatement checkRequestStmt = conn.prepareStatement(checkRequestQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // 🔍 1. Check if student has already requested this book
            checkRequestStmt.setString(1, book.getIsbn());
            checkRequestStmt.setString(2, student_id.getText());
            ResultSet rsRequest = checkRequestStmt.executeQuery();
            if (rsRequest.next() && rsRequest.getInt(1) > 0) {
                alert.errorMessage("You have already requested this book.");
                return;
            }

            // ✅ 2. Try to insert — if trigger blocks it, handle it in catch
            insertStmt.setString(1, book.getIsbn());
            insertStmt.setString(2, student_id.getText());
            insertStmt.setString(3, book.getTitle());
            insertStmt.setString(4, book.getAuthor());

            int result = insertStmt.executeUpdate();
            if (result > 0) {
                alert.successMessage("Your book request has been submitted.");
                Platform.runLater(() -> {
                    handleRequest(); // Refresh the table
                });
            } else {
                alert.errorMessage("Failed to submit your book request.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Quantity is zero")) { //checks trigger named prevent_request_if_quantity_zero
                alert.errorMessage("This book is currently out of stock.");
            } else {
                e.printStackTrace();  // Keep for debugging
                alert.errorMessage("Database error occurred.");
            }
        }

    }




    /*@Override
    public void initialize(URL location, ResourceBundle resources) {

        library_form.setVisible(false);
        teacher_form1.setVisible(false);
        teacher_form2.setVisible(false);
        bookrequest_form.setVisible(false);
        tracker_form.setVisible(false);
        studentIDDisplay(); // Ensure student ID is set first
        teacherDisplayData();
        showLoggedInStudentInfo();

        // Then display teacher data for that student

        System.out.println("Initialization complete.");

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBooks();// Debug print
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Existing stuff
        showApprovedBtn.setVisible(false);
        returnBookBtn.setVisible(false);

        library_form.setVisible(false);
        teacher_form1.setVisible(false);
        teacher_form2.setVisible(false);
        bookrequest_form.setVisible(false);
        tracker_form.setVisible(false);
        addTask_form.setVisible(false);
        taskList_form.setVisible(false);

        studentIDDisplay(); // Ensure student ID is set first
        teacherDisplayData();
        showLoggedInStudentInfo();

        System.out.println("Initialization complete.");

        // Set up library table
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBooks();

        // ✅ FIX HERE: Set up task table columns
        colTitlee.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colDueDatee.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colStatuss.setCellValueFactory(new PropertyValueFactory<>("status"));

        priorityComboBox.setItems(FXCollections.observableArrayList("High", "Medium", "Low"));

        // Initially hide task forms
        addTask_form.setVisible(false);
        taskList_form.setVisible(false);
        tracker_form.setVisible(false); // Start with tracker hidden

        // Load initial data
        loadStudentTasks();
        bookRequestsListData = FXCollections.observableArrayList();


    }

    @FXML private Label taskCompletedLabel;
    @FXML private Label taskPendingLabel;

    @FXML
    private TextField searchField1;
    @FXML
    private TextField searchField11;

    public void handleSearch1(ActionEvent actionEvent) {
        String searchText = searchField1.getText().trim();

        // ✅ Updated query: Ensure only books where date_delete IS NULL are included
        String sql = "SELECT * FROM books WHERE (isbn LIKE ? OR title LIKE ? OR author LIKE ?) AND date_delete IS NULL";

        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(sql)) {

            // Set parameters for prepared statement
            prepare.setString(1, searchText + "%");  // ISBN
            prepare.setString(2, "%" + searchText + "%");  // Title
            prepare.setString(3, "%" + searchText + "%");  // Author

            ResultSet result = prepare.executeQuery();
            ObservableList<BookData> filteredList = FXCollections.observableArrayList();

            while (result.next()) {
                BookData bData = new BookData(
                        null,
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("category"),
                        result.getString("isbn"),
                        null,
                        result.getString("status"),
                        null,
                        null,  // Assuming dateUpdated is not fetched here
                        null   // Assuming dateDeleted is not fetched here
                );
                filteredList.add(bData);
            }


            libraryTableView.setItems(filteredList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle potential SQL exceptions here
        }
    }

    @FXML
    private TableView<bookrequestData> bookrequestTableView;
    @FXML
    private TableColumn<bookrequestData, String> colRequestId;
    @FXML
    private TableColumn<bookrequestData, String> colRequestTitle;
    @FXML
    private TableColumn<bookrequestData, String> colRequestAuthor;
    @FXML
    private TableColumn<bookrequestData, String> colRequestStatus;
    @FXML
    private TableColumn<bookrequestData, String> colRequestIsbn;
    @FXML
    private TableColumn<bookrequestData, String> colDueDate;
    @FXML
    private TableColumn<bookrequestData, String> colIfRejected;

    private ObservableList<bookrequestData> bookRequestsListData;

    public void handleRequest() {
        bookRequestsListData.clear();
        bookRequestsListData = getBookRequestsData(); // Refresh data


        colRequestId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        colRequestTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colRequestAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colRequestStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRequestIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colIfRejected.setCellValueFactory(new PropertyValueFactory<>("ifRejected"));

        bookrequestTableView.setItems(bookRequestsListData);
    }


    /*public ObservableList<bookrequestData> getBookRequestsData() {
        ObservableList<bookrequestData> listData = FXCollections.observableArrayList();

        String query = "SELECT br.request_id, br.title, br.author, br.status, br.isbn, br.due_date, br.if_rejected " +
                "FROM book_requests br " +
                "INNER JOIN books b ON br.isbn = b.isbn " +
                "WHERE b.date_delete IS NULL AND br.student_id = ?";

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // ✅ Set the student ID from the UI label
            pstmt.setString(1, student_id.getText());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listData.add(new bookrequestData(
                        rs.getInt("request_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status"),
                        rs.getString("isbn"),
                        rs.getDate("due_date"),
                        rs.getString("if_rejected")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Debug info
        }

        return listData;
    }*/
    /*public ObservableList<bookrequestData> getBookRequestsData() {
        ObservableList<bookrequestData> listData = FXCollections.observableArrayList();

        String query = "SELECT br.request_id, br.title, br.author, br.status, br.isbn, br.due_date, br.if_rejected, " +
                "TRIM(br.isbn) AS br_isbn_trimmed, TRIM(b.isbn) AS book_isbn_trimmed " +
                "FROM book_requests br " +
                "INNER JOIN books b ON TRIM(br.isbn) = TRIM(b.isbn) " +  // 🔥 critical fix
                "WHERE br.student_id = ? AND b.date_delete IS NULL";

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, student_id.getText());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // 🔍 Print ISBNs to verify
                System.out.println("DEBUG ISBN MATCH => br.isbn: '" + rs.getString("br_isbn_trimmed") +
                        "' vs b.isbn: '" + rs.getString("book_isbn_trimmed") + "'");

                listData.add(new bookrequestData(
                        rs.getInt("request_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status"),
                        rs.getString("isbn"),
                        rs.getDate("due_date"),
                        rs.getString("if_rejected")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listData;
    }*/

    public ObservableList<bookrequestData> getBookRequestsData() {
        ObservableList<bookrequestData> listData = FXCollections.observableArrayList();

        String query = "SELECT br.request_id, br.title, br.author, br.status, br.isbn, br.due_date, br.if_rejected, " +
                "TRIM(br.isbn) AS br_isbn_trimmed, TRIM(b.isbn) AS book_isbn_trimmed, br.request_date " +
                "FROM book_requests br " +
                "INNER JOIN books b ON TRIM(br.isbn) = TRIM(b.isbn) " +
                "WHERE br.student_id = ? AND b.date_delete IS NULL " +
                "ORDER BY br.request_date ASC";  // 👈 This will place older ones on top, newer below

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, student_id.getText());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("DEBUG ISBN MATCH => br.isbn: '" + rs.getString("br_isbn_trimmed") +
                        "' vs b.isbn: '" + rs.getString("book_isbn_trimmed") + "'");

                listData.add(new bookrequestData(
                        rs.getInt("request_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status"),
                        rs.getString("isbn"),
                        rs.getDate("due_date"),
                        rs.getString("if_rejected")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listData;
    }


    public StudentData getLoggedInStudentData() {
        StudentData studentData = null;
        String query = "SELECT * FROM student WHERE student_id = ? AND date_delete IS NULL";

        try (Connection conn = Database.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, student_id.getText());

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                Date birthDate = result.getDate("birth_date"); // ✅ Moved inside `if (result.next())`

                studentData = new StudentData(
                        result.getInt("id"),
                        result.getString("student_id"),
                        result.getString("full_name"),
                        result.getString("gender"),
                        birthDate,
                        result.getInt("age"),
                        result.getString("year"),
                        result.getString("course"),
                        result.getString("section"),
                        result.getString("semester"),
                        result.getDouble("payment"),
                        result.getString("status_payment"),
                        result.getString("image"),
                        null,
                        null,
                        null,
                        null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentData;
    }

    @FXML
    private Label label_fullName;

    @FXML
    private Label label_gender;

    @FXML
    private Label label_birthDate;

    @FXML
    private Label label_age;

    @FXML
    private Label label_program;

    @FXML
    private Label label_semester;

    @FXML
    private Label label_section;

    @FXML
    private Label label_paymentStatus;

    @FXML
    private Label label_paymentAmount;

    @FXML
    private javafx.scene.shape.Circle student_profile_image;

    public void showLoggedInStudentInfo() {
        StudentData student = getLoggedInStudentData();

        if (student != null) {
            label_fullName.setText(student.getFullName());
            label_gender.setText(student.getGender()); // assuming getter and DB column exists
            label_birthDate.setText(String.valueOf(student.getBirthDate()));
            label_age.setText(String.valueOf(student.getAge()));
            label_program.setText(student.getCourse()); // or getProgram() if renamed
            label_semester.setText(student.getSemester());
            label_section.setText(student.getSection());
            label_paymentStatus.setText(student.getStatusPayment());
            label_paymentAmount.setText(String.valueOf(student.getPayment()));

            // Optionally load image if path exists:
            if (student.getImage() != null) {
                Image img = new Image("file:" + student.getImage(), false);
                student_profile_image.setFill(new ImagePattern(img));
            }
        }
    }


    public void showAddTaskForm(ActionEvent actionEvent) {
    }

    public void showTaskListForm(ActionEvent actionEvent) {
    }
    private void loadStudentTasks() {
        ObservableList<TaskData> taskList = FXCollections.observableArrayList();
        String query = "SELECT task_title, task_description, priority, due_date, status FROM student_todo WHERE student_id = ?";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student_id.getText());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                taskList.add(new TaskData(
                        rs.getString("task_title"),
                        rs.getString("task_description"),
                        rs.getString("priority"),
                        rs.getDate("due_date").toString(),
                        rs.getString("status")
                ));
            }

            todoTableView.setItems(taskList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markTaskCompleteAction(ActionEvent actionEvent) {
        TaskData selectedTask = todoTableView.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            alert.errorMessage("Please select a task to mark as completed.");
            return;
        }

        String updateSQL = "UPDATE student_todo SET status = 'Completed' " +
                "WHERE student_id = ? AND task_title = ? AND due_date = ?";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setString(1, student_id.getText());
            stmt.setString(2, selectedTask.getTitle());
            stmt.setDate(3, Date.valueOf(selectedTask.getDueDate()));

            int result = stmt.executeUpdate();
            if (result > 0) {
                alert.successMessage("Task marked as completed!");
                loadStudentTasks();  // Refresh table
            } else {
                alert.errorMessage("Could not update task. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Database error while updating task.");
        }
    }

    @FXML
    private Button  showAddTaskFormBtn;
    @FXML
    private Button  showTaskListBtn;

    @FXML
    private Button showStatus ;

    @FXML
    private AnchorPane addTask_form;

    @FXML
    private AnchorPane taskList_form;

    @FXML
    private AnchorPane status_form;

    public void switchForm2(ActionEvent actionEvent) {
        // First ensure tracker form is visible
        tracker_form.setVisible(true);

        // Then handle the task forms
        if (actionEvent.getSource() == showAddTaskFormBtn) {
            addTask_form.setVisible(true);
            taskList_form.setVisible(false);
            status_form.setVisible(false);
            clearTaskFields();
        }
        else if (actionEvent.getSource() == showTaskListBtn) {
            taskList_form.setVisible(true);
            addTask_form.setVisible(false);
            status_form.setVisible(false);
            loadStudentTasks(); // Refresh task list when showing
        }

        else if (actionEvent.getSource() == showStatus) {
            taskList_form.setVisible(false);
            addTask_form.setVisible(false);
            status_form.setVisible(true);
            //  loadStudentTasks(); // Refresh task list when showing
            Map<String, Integer> summary = getTaskSummaryByStatus();
            taskCompletedLabel.setText("Completed: " + summary.getOrDefault("Completed", 0));
            taskPendingLabel.setText("Pending: " + summary.getOrDefault("Pending", 0));
        }



        // Hide other main forms
        library_form.setVisible(false);
        teacher_form1.setVisible(false);
        teacher_form2.setVisible(false);
        bookrequest_form.setVisible(false);
        student_info_form.setVisible(false);
    }

    public void handleSearch11(ActionEvent actionEvent) {
        String search = searchField11.getText().trim().toLowerCase();

        ObservableList<bookrequestData> filteredList = FXCollections.observableArrayList();

        String query = "SELECT br.request_id, br.title, br.author, br.status, br.isbn, br.due_date, br.if_rejected " +
                "FROM book_requests br " +
                "JOIN books b ON br.isbn = b.isbn " +
                "WHERE br.student_id = ? " +
                "AND b.date_delete IS NULL " +
                "AND (LOWER(TRIM(br.title)) LIKE ? " +
                "OR LOWER(TRIM(br.author)) LIKE ? " +
                "OR LOWER(TRIM(br.isbn)) LIKE ? " +
                "OR LOWER(TRIM(br.status)) LIKE ?)";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String keyword = "%" + search + "%";

            // Set parameters
            stmt.setString(1, student_id.getText()); // Current student's ID
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);
            stmt.setString(4, keyword);
            stmt.setString(5, keyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                filteredList.add(new bookrequestData(
                        rs.getInt("request_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status"),
                        rs.getString("isbn"),
                        rs.getDate("due_date"),
                        rs.getString("if_rejected")
                ));
            }

            bookrequestTableView.setItems(filteredList);

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Error searching book requests");
        }
    }

    /*class TaskData {
        private final SimpleStringProperty title;
        private final SimpleStringProperty description;
        private final SimpleStringProperty priority;
        private final SimpleStringProperty dueDate;
        private final SimpleStringProperty status;

        public TaskData(String title, String description, String priority, String dueDate, String status) {
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty(description);
            this.priority = new SimpleStringProperty(priority);
            this.dueDate = new SimpleStringProperty(dueDate);
            this.status = new SimpleStringProperty(status);
        }

        public String getTitle() {
            return title.get();
        }

        public String getDescription() {
            return description.get();
        }

        public String getPriority() {
            return priority.get();
        }

        public String getDueDate() {
            return dueDate.get();
        }

        public String getStatus() {
            return status.get();
        }
    }*/

    public static class TaskData {
        private final SimpleStringProperty title;
        private final SimpleStringProperty description;
        private final SimpleStringProperty priority;
        private final SimpleStringProperty dueDate;
        private final SimpleStringProperty status;

        public TaskData(String title, String description, String priority, String dueDate, String status) {
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty(description);
            this.priority = new SimpleStringProperty(priority);
            this.dueDate = new SimpleStringProperty(dueDate);
            this.status = new SimpleStringProperty(status);
        }

        public String getTitle() {
            return title.get();
        }

        public String getDescription() {
            return description.get();
        }

        public String getPriority() {
            return priority.get();
        }

        public String getDueDate() {
            return dueDate.get();
        }

        public String getStatus() {
            return status.get();
        }
    }



    @FXML
    private TextField taskTitleField;
    @FXML
    private TextArea taskDescriptionField;
    @FXML
    private ComboBox<String> priorityComboBox;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TableView<TaskData> todoTableView;
    @FXML
    private TableColumn<TaskData, String> colTitlee;
    @FXML
    private TableColumn<TaskData, String> colDescription;
    @FXML
    private TableColumn<TaskData, String> colPriority;
    @FXML
    private TableColumn<TaskData, String> colDueDatee;
    @FXML
    private TableColumn<TaskData, String> colStatuss;


    @FXML
    private AnchorPane viewTasks_form;

    /*public void addTaskAction(ActionEvent event) {
        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();
        String priority = priorityComboBox.getSelectionModel().getSelectedItem();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || description.isEmpty() || priority == null || dueDate == null) {
            alert.errorMessage("Please fill all fields.");
            return;
        }

        String insertSQL = "INSERT INTO student_todo (student_id, task_title, task_description, priority, due_date, status) VALUES (?, ?, ?, ?, ?, 'Pending')";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, student_id.getText());
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, priority);
            stmt.setDate(5, Date.valueOf(dueDate));

            int result = stmt.executeUpdate();
            if (result > 0) {
                alert.successMessage("Task added successfully!");
                clearTaskFields();
                loadStudentTasks();
            } else {
                alert.errorMessage("Failed to add task.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Database error.");
        }
    }*/

    public void addTaskAction(ActionEvent event) {
        String title = taskTitleField.getText();
        String description = taskDescriptionField.getText();
        String priority = priorityComboBox.getSelectionModel().getSelectedItem();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || description.isEmpty() || priority == null || dueDate == null) {
            alert.errorMessage("Please fill all fields.");
            return;
        }

        String callSQL = "{CALL AddTask(?, ?, ?, ?, ?)}"; //calling procedure

        try (Connection conn = Database.connectDB();
             CallableStatement stmt = conn.prepareCall(callSQL)) {

            stmt.setString(1, student_id.getText());
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, priority);
            stmt.setDate(5, Date.valueOf(dueDate));

            stmt.execute();
            alert.successMessage("Task added using stored procedure!");
            clearTaskFields();
            loadStudentTasks();

        } catch (SQLException e) {
            if (e.getMessage().contains("Due date cannot be in the past")) {
                alert.errorMessage("Due date cannot be in the past.");
            } else {
                e.printStackTrace(); // Optional: debugging
                alert.errorMessage("Error adding task via procedure.");
            }
        }
    }



    private void clearTaskFields() {
        taskTitleField.clear();
        taskDescriptionField.clear();
        priorityComboBox.getSelectionModel().clearSelection();
        dueDatePicker.setValue(null);
    }

    public Map<String, Integer> getTaskSummaryByStatus() {
        Map<String, Integer> summary = new HashMap<>();
        String sql = "SELECT status, COUNT(*) AS total FROM student_todo WHERE student_id = ? GROUP BY status";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student_id.getText()); // student's ID from label
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                summary.put(rs.getString("status"), rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return summary;
    }

    @FXML
    private Label labelTotalCompleted;

    @FXML
    private Label labelTotalPending;

    /*private void updateTaskSummary() {
        Map<String, Integer> summary = getTaskStatusSummary(student_id.getText());

        int completed = summary.getOrDefault("Completed", 0);
        int pending = summary.getOrDefault("Pending", 0);

        labelTotalCompleted.setText("Completed: " + completed);
        labelTotalPending.setText("Pending: " + pending);
    }*/
    @FXML
    private TextField searchTaskField;



    public void searchTasksByString(ActionEvent event) {
        String search = searchTaskField.getText().trim().toLowerCase(); // Normalize input

        ObservableList<TaskData> searchList = FXCollections.observableArrayList();
        String query = "SELECT task_title, task_description, priority, due_date, status " +
                "FROM student_todo " +
                "WHERE student_id = ? AND (" +
                "LOWER(TRIM(task_title)) LIKE ? " +
                "OR LOWER(TRIM(task_description)) LIKE ? " +
                "OR LOWER(TRIM(CONCAT(task_title, ' ', task_description))) LIKE ?)";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String keyword = "%" + search + "%";

            stmt.setString(1, student_id.getText());
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);
            stmt.setString(4, keyword);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                searchList.add(new TaskData(
                        rs.getString("task_title"),
                        rs.getString("task_description"),
                        rs.getString("priority"),
                        rs.getDate("due_date").toString(),
                        rs.getString("status")
                ));
            }

            todoTableView.setItems(searchList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML private Button showApprovedBtn;
    @FXML private Button returnBookBtn;

    @FXML
    private void filterApprovedRequests() {
        ObservableList<bookrequestData> approvedList = FXCollections.observableArrayList();

        String sql = "SELECT br.request_id, br.title, br.author, br.status, br.isbn, br.due_date, br.if_rejected " +
                "FROM book_requests br " +
                "JOIN books b ON TRIM(br.isbn) = TRIM(b.isbn) " +
                "WHERE br.student_id = ? " +
                "AND br.status = 'Approved' " +
                "AND br.return_date IS NULL " +
                "AND b.date_delete IS NULL";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student_id.getText());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                approvedList.add(new bookrequestData(
                        rs.getInt("request_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status"),
                        rs.getString("isbn"),
                        rs.getDate("due_date"),
                        rs.getString("if_rejected")
                ));
            }

            bookrequestTableView.setItems(approvedList);
            returnBookBtn.setVisible(true);   //  show this
            showApprovedBtn.setVisible(false);
            returnAllBtn.setVisible(true);//  hide this

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Error filtering approved books.");
        }
    }


    /*@FXML
    private void handleReturnBook() {
        bookrequestData selected = bookrequestTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            alert.errorMessage("Please select a book to return.");
            return;
        }

        String updateSQL = "UPDATE book_requests SET return_date = CURDATE() WHERE request_id = ? AND status = 'Approved'";

        try (Connection conn = Database.connectDB();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setInt(1, selected.getRequestId());
            int result = stmt.executeUpdate();

            if (result > 0) {
                alert.successMessage("Book returned successfully!");
                handleRequest(); // reloads the full list
                returnBookBtn.setVisible(false);  // ✅ hide return
                showApprovedBtn.setVisible(true); // 🔁 bring back show approved
            } else {
                alert.errorMessage("Could not return book.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Database error while returning book.");
        }
    }*/

    @FXML
    private void handleReturnBook() {
        bookrequestData selected = bookrequestTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            alert.errorMessage("Please select a book to return.");
            return;
        }

        String updateRequestSQL = "UPDATE book_requests SET return_date = CURDATE() " +
                "WHERE request_id = ? AND status = 'Approved' AND return_date IS NULL";

        String updateBookQtySQL = "UPDATE books SET quantity = quantity + 1 " +
                "WHERE isbn = ? AND date_delete IS NULL";

        try (Connection conn = Database.connectDB()) {
            conn.setAutoCommit(false); // Start transaction

            // ✅ 1. Mark the book request as returned
            try (PreparedStatement stmt1 = conn.prepareStatement(updateRequestSQL)) {
                stmt1.setInt(1, selected.getRequestId());
                int updatedRows = stmt1.executeUpdate();

                if (updatedRows == 0) {
                    alert.errorMessage("Book return failed. Maybe it was already returned?");
                    conn.rollback();
                    return;
                }
            }


            try (PreparedStatement stmt2 = conn.prepareStatement(updateBookQtySQL)) {
                stmt2.setString(1, selected.getIsbn());
                stmt2.executeUpdate();
            }

            conn.commit(); // All successful ✅
            alert.successMessage("Book returned and quantity updated!");
            handleRequest(); // refresh table
            returnBookBtn.setVisible(false);
            showApprovedBtn.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Error returning book.");
        }
    }


    //batch return

    @FXML private Button returnAllBtn;

    @FXML
    private void handleReturnAll() {
        boolean hasReturnableBooks = false;
        for (bookrequestData item : bookrequestTableView.getItems()) {
            if ("Approved".equalsIgnoreCase(item.getStatus()) && item.getReturnDate() == null) {
                hasReturnableBooks = true;
                break;
            }
        }

        if (!hasReturnableBooks) {
            alert.errorMessage("No approved books to return.");
            return;
        }

        try (Connection conn = Database.connectDB(); // calling cursor
             CallableStatement stmt = conn.prepareCall("{CALL Return_All_ApprovedBooks(?)}")) {

            stmt.setString(1, student_id.getText());
            stmt.execute();

            alert.successMessage("All approved books returned!");
            handleRequest(); // 🔄 Refresh table

            returnAllBtn.setVisible(false);
            returnBookBtn.setVisible(false);
            showApprovedBtn.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Error during batch return.");
        }
    }










}
