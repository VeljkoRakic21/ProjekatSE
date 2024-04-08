package com.example.projekatse;

import java.time.LocalDate;


public class RentItem {
    private int rentId; // Include rent ID
    private LocalDate rentDate;
    private LocalDate dueDate; // Include due date
    private int bookID; // Modified to store book ID
    private String bookName;
    private String bookAuthor;
    private String genre; // New property for genre
    private int bookPrice;
    private String renterName;
    private String renterLastName;
    private byte[] pictureBytes;

    public RentItem(int rentId, LocalDate rentDate, LocalDate dueDate, int bookId, String bookName, String bookAuthor, String genre, int bookPrice, String renterName, String renterLastName, byte[] pictureBytes) {
        this.rentId = rentId;
        this.rentDate = rentDate;
        this.dueDate = dueDate; // Assign due date
        this.bookID = bookId; // Assign book ID
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.genre = genre; // Assign genre
        this.bookPrice = bookPrice;
        this.renterName = renterName;
        this.renterLastName = renterLastName;
        this.pictureBytes = pictureBytes;
    }

    // Getters for rent item properties
    public int getRentId() {
        return rentId;
    }

    public LocalDate getRentDate() {
        return rentDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getGenre() {
        return genre;
    }

    public int getBookPrice() {
        return bookPrice;
    }

    public String getRenterName() {
        return renterName;
    }

    public String getRenterLastName() {
        return renterLastName;
    }

    public byte[] getPictureBytes() {
        return pictureBytes;
    }
}

