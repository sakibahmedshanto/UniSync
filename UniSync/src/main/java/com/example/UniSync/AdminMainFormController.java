/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.UniSync;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author WINDOWS 10
 */
public class AdminMainFormController implements Initializable {

    @FXML
    private Button logout_btn;

    @FXML
    private Label greet_username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button addStudent_btn;

    @FXML
    private Button addTeacher_btn;

    @FXML
    private Button addCourse_btn;

    @FXML
    private Button addSubject_btn;

    @FXML
    private Button payment_btn;

    @FXML
    private Button salary_btn;

    @FXML
    private Button library_btn;

    @FXML
    private Button requestBtn;


    @FXML
    private Button lib_but;


    @FXML
    private AnchorPane addStudent_form;

    @FXML
    private AnchorPane addTeacher_form;


    @FXML
    private Button addStudent_deleteBtn;

    @FXML
    private Button addStudent_updateBtn;

    @FXML
    private Button addStudent_addBtn;

    @FXML
    private Button addbook_addBtn1;

    @FXML
    private TableView<TeacherData> addTeacher_tableView;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_teacherID;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_name;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_gender;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_yearExperience;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_experience;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_department;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_salary;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_dateInsert;

    @FXML
    private TableColumn<TeacherData, String> addTeacher_col_status;

    @FXML
    private TextField addTeacher_teacherID;

    @FXML
    private TextField addTeacher_fullName;

    @FXML
    private ComboBox<String> addTeacher_gender;

    @FXML
    private ComboBox<String> addTeacher_experienceYear;

    @FXML
    private TextField addTeacher_experience;

    @FXML
    private ComboBox<String> addTeacher_department;

    @FXML
    private TextField addTeacher_salary;

    @FXML
    private ComboBox<String> addTeacher_status;

    @FXML
    private ImageView addTeacher_imageView;

    @FXML
    private Button addTeacher_importBtn;

    @FXML
    private Button addTeacher_addBtn;

    @FXML
    private Button addTeacher_updateBtn;

    @FXML
    private Button addTeacher_clearBtn;

    @FXML
    private Button addTeacher_deleteBtn;

    @FXML
    private TextField addCourse_course;

    @FXML
    private TextField addCourse_department;

    @FXML
    private TextField addCourse_price;

    @FXML
    private ComboBox<String> addCourse_status;

    @FXML
    private Button addCourse_addBtn;

    @FXML
    private Button addCourse_updateBtn;

    @FXML
    private Button addCourse_clearBtn;

    @FXML
    private Button addCourse_deleteBtn;


    @FXML
    private AnchorPane addCourse_form;

    @FXML
    private AnchorPane addSubject_form;

    @FXML
    private TextField addSubject_code;

    @FXML
    private TextField addSubject_subject;

    @FXML
    private ComboBox<String> addSubject_course;

    @FXML
    private ComboBox<String> addSubject_status;

    @FXML
    private Button addSubject_addBtn;

    @FXML
    private Button addSubject_updateBtn;

    @FXML
    private Button addSubject_clearBtn;

    @FXML
    private Button addSubject_deleteBtn;

    @FXML
    private TableView<SubjectData> addSubject_tableView;

    @FXML
    private TableColumn<SubjectData, String> addSubject_col_code;

    @FXML
    private TableColumn<SubjectData, String> addSubject_col_subject;

    @FXML
    private TableColumn<SubjectData, String> addSubject_col_course;

    @FXML
    private TableColumn<SubjectData, String> addSubject_col_dateInsert;

    @FXML
    private TableColumn<SubjectData, String> addSubject_col_status;

    @FXML
    private AnchorPane payment_form;

    @FXML
    private TextField payment_studentID;

    @FXML
    private TextField payment_name;

    @FXML
    private TextField payment_year;

    @FXML
    private TextField payment_section;

    @FXML
    private TextField payment_semester;

    @FXML
    private TextField payment_payment;

    @FXML
    private ComboBox<String> payment_status;

    @FXML
    private ComboBox<String> addTeacher_salaryStatus;

    @FXML
    private ImageView payment_imageView;

    @FXML
    private Button payment_payBtn;

    @FXML
    private Button payment_clearBtn;


    @FXML
    private AnchorPane salary_form;

    @FXML
    private DatePicker salary_fromDate;

    @FXML
    private DatePicker salary_toDate;

    @FXML
    private TextField salary_teacherID;

    @FXML
    private TextField salary_name;

    @FXML
    private TextField salary_salaryPerDay;

    @FXML
    private ComboBox<String> salary_status;

    @FXML
    private Label salary_totalDays;

    @FXML
    private Label salary_salary;

    @FXML
    private Button salary_payBtn;

    @FXML
    private Button salary_clearBtn;
    @FXML
    private Button managing_books;

    @FXML
    private TableView<TeacherData> salary_tableView;

    @FXML
    private TableColumn<TeacherData, String> salary_col_teacherID;

    @FXML
    private TableColumn<TeacherData, String> salary_col_fullName;

    @FXML
    private TableColumn<TeacherData, String> salary_col_salaryPerDay;

    @FXML
    private TableColumn<TeacherData, String> salary_col_gender;

    @FXML
    private TableColumn<TeacherData, String> salary_col_dateInsert;

    @FXML
    private TableColumn<TeacherData, String> salary_col_dateUpdate;

    @FXML
    private TableColumn<TeacherData, String> salary_col_status;


    @FXML
    private AnchorPane dashboard_form;


    @FXML
    private AnchorPane request_form;

    @FXML
    private Label dashboard_TS;

    @FXML
    private Label dashboard_TT;

    @FXML
    private Label dashboard_SRT;

    @FXML
    private Label dashboard_TI;

    @FXML
    private AreaChart<?, ?> dashboard_chart_DS;

    @FXML
    private BarChart<?, ?> dashboard_chart_DT;

    @FXML
    private LineChart<?, ?> dashboard_chart_DI;


    @FXML
    private AnchorPane library_form;


    // DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private AlertMessage alert = new AlertMessage();

    private Image image;


    public ObservableList<TeacherData> addTeacherGetData() {

        ObservableList<TeacherData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM teacher WHERE date_delete IS NULL";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            TeacherData tData;

            while (result.next()) {
//                TeacherData(Integer id, String teacherID, String fullName, String gender, Date birthDate,
//            String yearExperience, String experience, String department, Double salary,
//            String image, Date dateInsert, Date dateUpdate, Date dateDelete, String status)
                tData = new TeacherData(result.getInt("id"),
                        result.getString("teacher_id"),
                        result.getString("full_name"), result.getString("gender"),
                        result.getString("year_experience"), result.getString("experience"),
                        result.getString("department"), result.getDouble("salary"),
                        result.getString("salary_status"),
                        result.getString("image"), result.getDate("date_insert"),
                        result.getDate("date_update"), result.getDate("date_delete"),
                        result.getString("status"));

                listData.add(tData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<TeacherData> addTeacherListData;

    public void addTeacherDisplayData() {
        addTeacherListData = addTeacherGetData();

        addTeacher_col_teacherID.setCellValueFactory(new PropertyValueFactory<>("teacherID"));
        addTeacher_col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        addTeacher_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addTeacher_col_yearExperience.setCellValueFactory(new PropertyValueFactory<>("yearExperience"));
        addTeacher_col_experience.setCellValueFactory(new PropertyValueFactory<>("experience"));
        addTeacher_col_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        addTeacher_col_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        addTeacher_col_dateInsert.setCellValueFactory(new PropertyValueFactory<>("dateInsert"));
        addTeacher_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        addTeacher_tableView.setItems(addTeacherListData);

    }

    public void addTeacherSelectItems() {

        TeacherData tData = addTeacher_tableView.getSelectionModel().getSelectedItem();
        int num = addTeacher_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addTeacher_teacherID.setText(tData.getTeacherID());
        addTeacher_fullName.setText(tData.getFullName());
        addTeacher_gender.getSelectionModel().select(tData.getGender());
        addTeacher_experienceYear.getSelectionModel().select(tData.getYearExperience());
        addTeacher_experience.setText(tData.getExperience());
        addTeacher_department.getSelectionModel().select(tData.getDepartment());
        addTeacher_salary.setText("" + tData.getSalary());
        addTeacher_status.getSelectionModel().select(tData.getStatus());
        addTeacher_salaryStatus.getSelectionModel().select(tData.getSalaryStatus());

        ListData.path = tData.getImage();

        image = new Image("File:" + tData.getImage(), 90, 103, false, true);
        addTeacher_imageView.setImage(image);
    }

    public void addTeacherGenderList() {

        List<String> listG = new ArrayList<>();

        for (String data : ListData.gender) {
            listG.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listG);
        addTeacher_gender.setItems(listData);
    }

    public void addTeacherEYList() {

        List<String> listEY = new ArrayList<>();

        for (String data : ListData.yearExperience) {
            listEY.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listEY);
        addTeacher_experienceYear.setItems(listData);
    }

    public void addTeacherSSList() {

        List<String> listSS = new ArrayList<>();

        for (String data : ListData.paymentStatus) {
            listSS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listSS);

        addTeacher_salaryStatus.setItems(listData);

    }

    public void addTeacherDepartmentList() {

//        List<String> listD = new ArrayList<>();
//
//        for (String data : ListData.department) {
//            listD.add(data);
//        }
//        ObservableList listData = FXCollections.observableArrayList(listD);
//        addTeacher_department.setItems(listData);
        String sql = "SELECT * FROM course WHERE date_delete IS NULL";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("department"));
            }
            addTeacher_department.setItems(listData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addTeacherStatusList() {

        List<String> listS = new ArrayList<>();

        for (String data : ListData.status) {
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);
        addTeacher_status.setItems(listData);
    }

    private String teacherID;


    public void addTeacherDisplayTeacherID() {
        addTeacher_teacherID.setText(ListData.loggedInTeacherID);
    }

    public void addTeacherAddBtn() {

        if (addTeacher_teacherID.getText().isEmpty()
                || addTeacher_fullName.getText().isEmpty()
                || addTeacher_gender.getSelectionModel().getSelectedItem().isEmpty()
                || addTeacher_experience.getText().isEmpty()
                || addTeacher_experienceYear.getSelectionModel().getSelectedItem().isEmpty()
                || addTeacher_department.getSelectionModel().getSelectedItem().isEmpty()
                || addTeacher_salary.getText().isEmpty()
                || addTeacher_status.getSelectionModel().getSelectedItem().isEmpty()
                || ListData.path == null || "".equals(ListData.path)
                || addTeacher_salaryStatus.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {

            String insertData = "INSERT INTO teacher "
                    + "(teacher_id, full_name, gender, year_experience, experience"
                    + ", department, salary, salary_status, image, date_insert, status) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            connect = Database.connectDB();

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String path = ListData.path;
            path = path.replace("\\", "\\\\");

            try {
                prepare = connect.prepareStatement(insertData);
                prepare.setString(1, addTeacher_teacherID.getText());
                prepare.setString(2, addTeacher_fullName.getText());
                prepare.setString(3, addTeacher_gender.getSelectionModel().getSelectedItem());
                prepare.setString(4, addTeacher_experienceYear.getSelectionModel().getSelectedItem());
                prepare.setString(5, addTeacher_experience.getText());
                prepare.setString(6, addTeacher_department.getSelectionModel().getSelectedItem());
                prepare.setString(7, addTeacher_salary.getText());
                prepare.setString(8, addTeacher_salaryStatus.getSelectionModel().getSelectedItem());
                prepare.setString(9, path);
                prepare.setString(10, String.valueOf(sqlDate));
                prepare.setString(11, addTeacher_status.getSelectionModel().getSelectedItem());

                prepare.executeUpdate();

                addTeacherDisplayData();

                Path transfer = Paths.get(path);
                Path copy = Paths.get("C:\\Users\\WINDOWS 10\\Documents\\NetBeansProjects\\UniversitiyManagementSystem\\src\\Teacher_Directory\\"
                        + addTeacher_teacherID.getText() + ".jpg");

                Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);

                alert.successMessage("Added Successfully!");

                addTeacherClearBtn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addTeacherUpdateBtn() {

        if (addTeacher_teacherID.getText().isEmpty()
                || addTeacher_fullName.getText().isEmpty()
                || addTeacher_gender.getSelectionModel().getSelectedItem().isEmpty()
                || addTeacher_experience.getText().isEmpty()
                || addTeacher_experienceYear.getSelectionModel().getSelectedItem().isEmpty()
                || addTeacher_department.getSelectionModel().getSelectedItem().isEmpty()
                || addTeacher_salary.getText().isEmpty()
                || addTeacher_status.getSelectionModel().getSelectedItem().isEmpty()
                || ListData.path == null || "".equals(ListData.path)
                || addTeacher_salaryStatus.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            if (alert.confirmMessage("Are you sure you want to Update Teacher ID: "
                    + addTeacher_teacherID.getText() + "?")) {
                String path = ListData.path;
                path = path.replace("\\", "\\\\");

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                String updateData = "UPDATE teacher SET full_name = '"
                        + addTeacher_fullName.getText() + "', gender = '"
                        + addTeacher_gender.getSelectionModel().getSelectedItem() + "', experience = '"
                        + addTeacher_experience.getText() + "', year_experience = '"
                        + addTeacher_experienceYear.getSelectionModel().getSelectedItem() + "', department = '"
                        + addTeacher_department.getSelectionModel().getSelectedItem() + "', salary = '"
                        + addTeacher_salary.getText() + "', salary_status = '"
                        + addTeacher_salaryStatus.getSelectionModel().getSelectedItem() + "', image = '"
                        + path + "', date_update = '"
                        + sqlDate + "', status = '"
                        + addTeacher_status.getSelectionModel().getSelectedItem() + "' "
                        + "WHERE teacher_id = '"
                        + addTeacher_teacherID.getText() + "'";

                connect = Database.connectDB();

                try {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    addTeacherDisplayData();

                    Path transfer = Paths.get(path);
                    Path copy = Paths.get("C:\\Users\\WINDOWS 10\\Documents\\NetBeansProjects\\UniversitiyManagementSystem\\src\\Teacher_Directory\\"
                            + addTeacher_teacherID.getText() + ".jpg");

                    Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);

                    alert.successMessage("Updated Successfully!");

                    addTeacherClearBtn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert.errorMessage("Cancelled.");
            }
        }
    }

    public void addTeacherDeleteBtn() {

        if (addTeacher_teacherID.getText().isEmpty()
                || addTeacher_status.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please select the item first");
        } else {

            if (alert.confirmMessage("Are you sure you want to Delete Teacher ID: "
                    + addTeacher_teacherID.getText() + "?")) {
                String deleteData = "UPDATE teacher SET date_delete = ? WHERE teacher_id = ?";
                connect = Database.connectDB();

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.setString(1, "" + sqlDate);
                    prepare.setString(2, addTeacher_teacherID.getText());

                    prepare.executeUpdate();

                    addTeacherDisplayData();

                    alert.successMessage("Deleted Successfully!");

                    addTeacherClearBtn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert.errorMessage("Cancelled");
            }
        }
    }

    public void addTeacherClearBtn() {
        addTeacherDisplayTeacherID();
        addTeacher_fullName.clear();
        addTeacher_gender.getSelectionModel().clearSelection();
        addTeacher_experienceYear.getSelectionModel().clearSelection();
        addTeacher_experience.clear();
        addTeacher_department.getSelectionModel().clearSelection();
        addTeacher_salary.clear();
        ListData.path = "";
        addTeacher_status.getSelectionModel().clearSelection();

        addTeacher_imageView.setImage(null);

    }

    public void addTeacherImportBtn() {

        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new ExtensionFilter("Open Image", "*.jpg", "*.jpeg", "*.png"));

        File file = open.showOpenDialog(addTeacher_importBtn.getScene().getWindow());

        if (file != null) {
            ListData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 90, 103, false, true);
            addTeacher_imageView.setImage(image);
        }
    }

    public ObservableList<SubjectData> addSubjectGetData() {

        ObservableList<SubjectData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM subject WHERE date_delete IS NULL";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            SubjectData sData;
//            SubjectData(Integer id, String subjectCode, String subject, String course,
//            Date dateInsert, Date dateUpdate, Date dateDelete, String status)
            while (result.next()) {
                sData = new SubjectData(result.getInt("id"),
                        result.getString("subject_code"),
                        result.getString("subject"),
                        result.getString("course"),
                        result.getDate("date_insert"),
                        result.getDate("date_update"),
                        result.getDate("date_delete"),
                        result.getString("status"));

                listData.add(sData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<SubjectData> addSubjectListData;

    public void addSubjectDisplayData() {

        addSubjectListData = addSubjectGetData();

        addSubject_col_code.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));
        addSubject_col_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        addSubject_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        addSubject_col_dateInsert.setCellValueFactory(new PropertyValueFactory<>("dateInsert"));
        addSubject_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        addSubject_tableView.setItems(addSubjectListData);
    }

    private int subjectID = 0;

    public void addSubjectSelectItem() {
        SubjectData sData = addSubject_tableView.getSelectionModel().getSelectedItem();
        int num = addSubject_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addSubject_code.setText(sData.getSubjectCode());
        addSubject_subject.setText(sData.getSubject());
        addSubject_course.getSelectionModel().select(sData.getCourse());
        addSubject_status.getSelectionModel().select(sData.getStatus());

        subjectID = sData.getId();
    }

    public void addSubjectAddBtn() {

        if (addSubject_code.getText().isEmpty()
                || addSubject_subject.getText().isEmpty()
                || addSubject_course.getSelectionModel().getSelectedItem().isEmpty()
                || addSubject_status.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            connect = Database.connectDB();

            String checkSubject = "SELECT * FROM subject WHERE subject_code = '"
                    + addSubject_code.getText() + "' AND date_delete IS NULL";
            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkSubject);

                if (result.next()) {
                    alert.errorMessage(addSubject_code.getText() + " is already exist");
                } else {
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    String insertData = "INSERT INTO subject "
                            + "(subject_code, subject, course, date_insert, status) "
                            + "VALUES(?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, addSubject_code.getText());
                    prepare.setString(2, addSubject_subject.getText());
                    prepare.setString(3, addSubject_course.getSelectionModel().getSelectedItem());
                    prepare.setString(4, String.valueOf(sqlDate));
                    prepare.setString(5, addSubject_status.getSelectionModel().getSelectedItem());

                    prepare.executeUpdate();

                    addSubjectDisplayData();

                    alert.successMessage("Added Successfully!");

                    addSubjectClear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addSubjectUpdateBtn() {

        if (addSubject_code.getText().isEmpty()
                || addSubject_subject.getText().isEmpty()
                || addSubject_course.getSelectionModel().getSelectedItem().isEmpty()
                || addSubject_status.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {
            if (alert.confirmMessage("Are you sure you want to Update the Subject Code: "
                    + addSubject_code.getText() + "?")) {
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                String updateData = "UPDATE subject SET subject_code = '"
                        + addSubject_code.getText() + "', subject = '"
                        + addSubject_subject.getText() + "', course = '"
                        + addSubject_course.getSelectionModel().getSelectedItem()
                        + "', date_update = '"
                        + sqlDate + "', status = '"
                        + addSubject_status.getSelectionModel().getSelectedItem() + "' "
                        + "WHERE id = " + subjectID;

                connect = Database.connectDB();

                try {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    addSubjectDisplayData();

                    alert.successMessage("Updated Successfully!");

                    addSubjectClear();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert.errorMessage("Cancelled");
            }
        }
    }

    public void addSubjectDeleteBtn() {

        if (subjectID == 0) {
            alert.errorMessage("Please select item first");
        } else {

            if (alert.confirmMessage("Are you sure you want to Delete Subject Code: "
                    + addSubject_code.getText() + "?")) {
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                String deleteData = "UPDATE subject SET date_delete = ? WHERE id = ?";
                connect = Database.connectDB();

                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.setString(1, String.valueOf(sqlDate));
                    prepare.setString(2, String.valueOf(subjectID));

                    prepare.executeUpdate();

                    addSubjectDisplayData();

                    alert.successMessage("Deleted Successfully!");

                    addSubjectClear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void addSubjectClear() {
        addSubject_code.clear();
        addSubject_subject.clear();
        addSubject_course.getSelectionModel().clearSelection();
        addSubject_status.getSelectionModel().clearSelection();
    }

    //
    public void addSubjectStatusList() {

        List<String> listS = new ArrayList<>();

        for (String data : ListData.statusA) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        addSubject_status.setItems(listData);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == addTeacher_btn) {
            dashboard_form.setVisible(false);
            addStudent_form.setVisible(false);
            addTeacher_form.setVisible(true);
            addCourse_form.setVisible(false);
            addSubject_form.setVisible(false);
            payment_form.setVisible(false);
            salary_form.setVisible(false);
            library_form.setVisible(false);
            request_form.setVisible(false);


            addTeacherDisplayData();
            addTeacherGenderList();
            addTeacherSSList();
            addTeacherEYList();
            addTeacherDepartmentList();
            addTeacherStatusList();
            addTeacherDisplayTeacherID();
        } else if (event.getSource() == addSubject_btn) {
            dashboard_form.setVisible(false);
            addStudent_form.setVisible(false);
            addTeacher_form.setVisible(false);
            addCourse_form.setVisible(false);
            addSubject_form.setVisible(true);
            payment_form.setVisible(false);
            salary_form.setVisible(false);
            library_form.setVisible(false);
            request_form.setVisible(false);

            addSubjectDisplayData();
            //addSubjectCourseList();
            addSubjectStatusList();
        }

        // Method to load library data
        // libraryStatusList();  // M

    }

    /*public void displayGreet() {
        String username = ListData.admin_username;
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        greet_username.setText("Welcome, " + username);
    }*/
    public void logoutBtn() {
        try {
            if (alert.confirmMessage("Are you sure you want to logout?")) {
                // TO SHOW THE LOGIN FORM
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();

                stage.setScene(new Scene(root));
                stage.show();

                // TO HIDE YOUR MAIN FORM
                logout_btn.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing dashboard...");

        if (dashboard_TS == null) {
            System.out.println("ERROR: dashboard_TS is NULL! Check FXML file.");
        } else {
            System.out.println("dashboard_TS is loaded!");
        }

        if (Database.connectDB() == null) {
            System.out.println("ERROR: Database connection failed!");
        } else {
            System.out.println("Database connected.");
        }
    }}
