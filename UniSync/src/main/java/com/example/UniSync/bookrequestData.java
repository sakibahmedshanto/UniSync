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
    //-------------------   new ---------------------- //
    private String AutoReq;
    //-------------------   new ---------------------- //
    private String ifRejected;

    //-------------------   new ---------------------- //
    public bookrequestData(int requestId, String studentId, String title, String author, String isbn, Date requestDate, String status, Date dueDate, Date returnDate, String AutoReq) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.requestDate = requestDate;
        this.status = status;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this. autoApprovalSuggested = AutoReq;
    }
    //-------------------   new ---------------------- //
    public bookrequestData(int requestId, String title, String author, String status, String isbn, Date dueDate, String ifRejected) {
        this.requestId = requestId;
        this.title = title;
        this.author = author;
        this.status = status;
        this.isbn = isbn;
        this.dueDate = dueDate;
        this.ifRejected = ifRejected;
    }


    // Getters
    // Change the field name and getter:
    private String autoApprovalSuggested;
    //-------------------   new ---------------------- //
    public String getAutoApprovalSuggested() {
        return autoApprovalSuggested;
    }

    public void setAutoApprovalSuggested(String autoApprovalSuggested) {
        this.autoApprovalSuggested = autoApprovalSuggested;
    }
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

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }
    public String getIfRejected() {
        return ifRejected;
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

    public String getAutoReq() {
        return AutoReq;
    }

    public void setAutoReq(String autoReq) {
        this.AutoReq = autoReq;
    }
    //-------------------   new ---------------------- //
    private Double fineAmount;

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    //-------------------   new ---------------------- // for fine
    public bookrequestData(int requestId, String studentId, String isbn, Date dueDate, Date returnDate, Double fineAmount) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.isbn = isbn;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
    }


}

