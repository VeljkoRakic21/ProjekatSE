package com.example.projekatse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static com.example.projekatse.Database.getUserId;



public class HistoryRentScene {

    private static String genre;


    public static void openHistoryRentScene(Stage primaryStage, String username) {
        // Message at the top
        Text historyMessage = new Text("Rent History");
        historyMessage.getStyleClass().add("history-message");
        historyMessage.setFill(Color.web("#673147")); // Set text color to #673147

        // Navigation bar
        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, username);
        navigationBar.getStyleClass().add("navigation-bar");

        // Layout setup
        VBox layout = new VBox(20, historyMessage);
        BorderPane root = new BorderPane(layout);
        root.getStyleClass().add("root");
        root.setBottom(navigationBar);
        layout.setAlignment(Pos.TOP_CENTER);

        // Fetch rent history data for the logged-in user
        List<RentItem> rentHistory = getRentHistory(username);

        // Create UI elements for rent history
        VBox rentHistoryBox = new VBox(10);
        rentHistoryBox.setAlignment(Pos.TOP_CENTER);
        for (RentItem item : rentHistory) {
            VBox rentItemBox = createRentItemBox(item, username);
            rentHistoryBox.getChildren().add(rentItemBox);
        }
        rentHistoryBox.setStyle("-fx-background-color: #e5d2b8;");

        // Add rent history to a scrollable pane
        ScrollPane scrollPane = new ScrollPane(rentHistoryBox);
        scrollPane.setFitToWidth(true);

        // Add scroll pane to layout
        layout.getChildren().add(scrollPane);

        // Create the scene
        Scene historyRentScene = new Scene(root, 600, 600);
        historyRentScene.getStylesheets().add(HistoryRentScene.class.getResource("stylehistory.css").toExternalForm());
        primaryStage.setScene(historyRentScene);
    }

    private static VBox createRentItemBox(RentItem rentItem, String username) {
        // Create an HBox to hold the book image and its details
        HBox rentItemBox = new HBox(10);
        rentItemBox.setAlignment(Pos.TOP_LEFT);
        rentItemBox.setPadding(new Insets(10));
        rentItemBox.getStyleClass().add("rent-item-box");

        // Load book image
        ImageView bookImageView = new ImageView(new Image(new ByteArrayInputStream(rentItem.getPictureBytes())));
        bookImageView.setFitWidth(80);
        bookImageView.setPreserveRatio(true);
        bookImageView.getStyleClass().add("image-view");

        // Create UI elements for rent item details
        VBox rentItemDetails = new VBox(5);
        rentItemDetails.setAlignment(Pos.TOP_LEFT);
        rentItemDetails.getStyleClass().add("rent-item-details");
        rentItemDetails.getChildren().addAll(
                new Text("Book Name: " + rentItem.getBookName()),
                new Text("Author: " + rentItem.getBookAuthor()),
                new Text("Price: $" + rentItem.getBookPrice()),
                new Text("Date Rented: " + rentItem.getRentDate()),
                new Text("Due Date: " + rentItem.getDueDate()),
                new Text("Rented by: " + rentItem.getRenterName() + " " + rentItem.getRenterLastName())
        );


        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(1, 2, 3, 4, 5);
        comboBox.getSelectionModel().selectFirst(); // Set default selection
        comboBox.setMinWidth(50);
        comboBox.getStyleClass().add("combobox");
        comboBox.setId("comboBox");


        // Create a submit button
        Button rateButton = new Button("Rate");
        rateButton.getStyleClass().add("rate-button");
        rateButton.setId("rate-button");


        rateButton.setOnAction(event -> {
            int rating = comboBox.getValue();
            int bookID = rentItem.getBookID();
            int rentId = rentItem.getRentId();

            submitRating(username, bookID, rating, rentId);
        });

        // Add book image, details, quantity combo box, and submit button to rent item box
        rentItemBox.getChildren().addAll(bookImageView, rentItemDetails, comboBox, rateButton);

        HBox rightBox = new HBox(10);
        rightBox.getChildren().addAll(comboBox, rateButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getStyleClass().add("right-box");

        // Add rightBox to rentItemBox
        rentItemBox.getChildren().add(rightBox);
        return new VBox(rentItemBox);
    }

    public static List<RentItem> getRentHistory(String username) {
        List<RentItem> rentHistory = new ArrayList<>();
        // Query the database to retrieve rent history for the logged-in user
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT RENT.ID_RENT, RENT.DATE, RENT.DUE_DATE, BOOK.ID_BOOK, BOOK.NAME, BOOK.AUTHOR, BOOK.PRICE, BOOK.PICTURE, USER.NAME, USER.LAST_NAME " +
                    "FROM RENT " +
                    "INNER JOIN BOOK ON RENT.ID_BOOK = BOOK.ID_BOOK " +
                    "INNER JOIN USER ON RENT.ID_USER = USER.ID_USER " +
                    "WHERE USER.USERNAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int rentId = resultSet.getInt("RENT.ID_RENT");
                        LocalDate rentDate = resultSet.getDate("RENT.DATE").toLocalDate();
                        LocalDate dueDate = resultSet.getDate("RENT.DUE_DATE") != null ? resultSet.getDate("RENT.DUE_DATE").toLocalDate() : null; // Retrieve due date, handle null
                        int bookId = resultSet.getInt("BOOK.ID_BOOK");
                        String bookName = resultSet.getString("BOOK.NAME");
                        String bookAuthor = resultSet.getString("BOOK.AUTHOR");
                        int bookPrice = resultSet.getInt("BOOK.PRICE");

                        // Retrieve the Blob picture
                        Blob pictureBlob = resultSet.getBlob("BOOK.PICTURE");
                        byte[] pictureBytes = null;
                        if (pictureBlob != null) {
                            pictureBytes = pictureBlob.getBytes(1, (int) pictureBlob.length());
                        }

                        String renterName = resultSet.getString("USER.NAME");
                        String renterLastName = resultSet.getString("USER.LAST_NAME");

                        RentItem rentItem = new RentItem(rentId, rentDate, dueDate, bookId, bookName, bookAuthor, genre, bookPrice, renterName, renterLastName, pictureBytes);
                        rentHistory.add(rentItem);
                    }
                }
            }
            System.out.println("Rent history retrieved successfully."); // Print statement after successful retrieval
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentHistory;
    }

    public static boolean hasUserRatedRentItem(int userId, int rentId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT COUNT(*) AS count FROM RATING WHERE ID_USER = ? AND ID_RENT = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                statement.setInt(2, rentId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        return count > 0; // Return true if the user has already rated this rent item
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to false if an exception occurs or no rows are returned
    }

    public static void submitRating(String username, int bookID, int rating, int rentId) {
        int userId = getUserId(username); // Retrieve userId using the username

        // Check if the user has already rated this rent item
        if (hasUserRatedRentItem(userId, rentId)) {
            InputValidator.showErrorDialog("You have already rated this rent item.");
            return;
        }

        // Insert into database
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "INSERT INTO RATING (ID_USER, ID_BOOK, RATING, ID_RENT) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookID);
            preparedStatement.setInt(3, rating);
            preparedStatement.setInt(4, rentId);
            preparedStatement.executeUpdate();

            // Retrieve the book name and author from the database
            String bookName = "";
            String authorName = "";
            String bookQuery = "SELECT NAME, AUTHOR FROM BOOK WHERE ID_BOOK = ?";
            try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery)) {
                bookStatement.setInt(1, bookID);
                try (ResultSet bookResultSet = bookStatement.executeQuery()) {
                    if (bookResultSet.next()) {
                        bookName = bookResultSet.getString("NAME");
                        authorName = bookResultSet.getString("AUTHOR");
                    }
                }
            }
            
            // Show success alert with book name, author name, and rating
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rating Submitted");
            alert.setHeaderText(null);
            alert.setContentText("You have rated " + bookName + " by " + authorName + " with a rating of " + rating + "/5.");
            Pane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(Database.class.getResource("stylealert.css").toExternalForm());
            alert.showAndWait();

            System.out.println("Rating submitted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
