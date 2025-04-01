package com.example.UniSync;

import java.util.Date;

public class bookrequestData {
    private int requestId;
    private String studentId;
    private String title;
    private String author;
    private String isbn;
    private Date requestDate;

    private Date dueDate;

    private Date returnDate;
    private String status;

    // Constructor
    public bookrequestData(int requestId, String studentId, String title, String author, String isbn, Date requestDate, String status, Date dueDate, Date returnDate) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.requestDate = requestDate;
        this.status = status;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    // Getters
    public int getRequestId( ) {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public Date getdueDate() {
        return dueDate;
    }

    public Date getreturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public void setdueDate(Date requestDate) {
        this.dueDate = dueDate;
    }

    public void setreturnDate(Date requestDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

