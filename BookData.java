/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universitiymanagementsystem;

import java.sql.Date;

/**
 *
 * @author WINDOWS 10
 */
public class BookData {

    private Integer bookId;
    private String book_title;
    private String author;
    private String category;
    private String isbn;
    private Integer quantity;
    private String status;
    private Date dateAdded;
    private Date dateUpdated;
    private Date dateDeleted;

    // Full Constructor (All Fields)
    public BookData(Integer bookId, String title, String author, String category, String isbn, Integer quantity, String status, Date dateAdded, Date dateUpdated, Date dateDeleted) {
        this.bookId = bookId;
        this.book_title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.quantity = quantity;
        this.status = status;
        this.dateAdded = dateAdded;
        this.dateUpdated = dateUpdated;
        this.dateDeleted = dateDeleted;
    }

    // Constructor for display (without date fields)
    public BookData(Integer bookId, String title, String author, String category, String isbn, Integer quantity, String status) {
        this.bookId = bookId;
        this.book_title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.quantity = quantity;
        this.status = status;
    }

    // Constructor for adding books
    public BookData(String title, String author, String category, String isbn, Integer quantity, String status) {
        this.book_title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.quantity = quantity;
        this.status = status;
    }

    // Constructor for updating books
    public BookData(Integer bookId, String title, String author, Integer quantity, String status, Date dateUpdated) {
        this.bookId = bookId;
        this.book_title = title;
        this.author = author;
        this.quantity = quantity;
        this.status = status;
        this.dateUpdated = dateUpdated;
    }

    // Getters
    public Integer getBookID() {
        return bookId;
    }

    public String getTitle() {
        return book_title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public Date getDateDeleted() {
        return dateDeleted;
    }

    // Setters (optional, only if needed)
    public void setTitle(String title) {
        this.book_title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
    }
}
