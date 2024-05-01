package com.example.projekatse;

import java.time.LocalDate;


public class RentItem {
    private int rentId;
    private LocalDate rentDate;
    private LocalDate dueDate;
    private int bookID;
    private String bookName;
    private String bookAuthor;
    private String genre;
    private int bookPrice;
    private String renterName;
    private String renterLastName;
    private byte[] pictureBytes;

    /**
     * Construct that has all data about rent items, getters and setter.
     * @param rentId
     * @param rentDate
     * @param dueDate
     * @param bookId
     * @param bookName
     * @param bookAuthor
     * @param genre
     * @param bookPrice
     * @param renterName
     * @param renterLastName
     * @param pictureBytes
     */
    public RentItem(int rentId, LocalDate rentDate, LocalDate dueDate, int bookId, String bookName, String bookAuthor, String genre, int bookPrice, String renterName, String renterLastName, byte[] pictureBytes) {
        this.rentId = rentId;
        this.rentDate = rentDate;
        this.dueDate = dueDate;
        this.bookID = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.genre = genre;
        this.bookPrice = bookPrice;
        this.renterName = renterName;
        this.renterLastName = renterLastName;
        this.pictureBytes = pictureBytes;
    }


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

