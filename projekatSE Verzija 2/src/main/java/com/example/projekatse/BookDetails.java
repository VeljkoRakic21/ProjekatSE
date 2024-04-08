package com.example.projekatse;

import java.sql.Blob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BookDetails {

    private int id;
    private String name;
    private String author;
    private String synopsis;
    private String genre;
    private Blob picture;
    private boolean availability;
    private int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPictureBytes() {
        if (picture != null) {
            try (InputStream inputStream = picture.getBinaryStream()) {
                byte[] bytes = new byte[(int) picture.length()];
                inputStream.read(bytes);
                return bytes;
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


