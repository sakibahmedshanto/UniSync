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

//p






}
