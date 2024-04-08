package com.example.projekatse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RecommendedBookScene {
    private static String genre;

    public static void openRecommendedBookScene(Stage primaryStage, String username) {
        // Text message
        Text recommendedMessage = new Text("Recommended Books");
        recommendedMessage.setFill(Color.rgb(103, 49, 71));
        recommendedMessage.getStyleClass().add("recommended-message");

        // Buttons
        Button genreButton = new Button("Genre ▼");
        genreButton.getStyleClass().add("genre-button");
        genreButton.setOnAction(event -> {
            // Action for Genre button
            // You can implement the functionality here
            List<BookDetails> allBooksSortedByGenre = getAllBooksSortedByGenre();
            displayBooks(primaryStage, username, allBooksSortedByGenre);
        });

        Button ratingButton = new Button("Rating ▼");
        ratingButton.getStyleClass().add("rating-button");
        ratingButton.setOnAction(event -> {
            // Action for Rating button
            List<BookDetails> allBooksSortedByRating = getAllBooksSortedByRating();
            displayBooksByRating(primaryStage, username, allBooksSortedByRating);
        });

        // Navigation bar
        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, username);

        // Layout setup
        VBox layout = new VBox(20, recommendedMessage, createButtonBar(genreButton, ratingButton));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");
        BorderPane root = new BorderPane(layout);
        root.setBottom(navigationBar);

        // Create the scene
        Scene recommendedBookScene = new Scene(root, 600, 600);
        recommendedBookScene.getStylesheets().add(RecommendedBookScene.class.getResource("stylerecommended.css").toExternalForm());
        primaryStage.setScene(recommendedBookScene);
    }

    private static HBox createButtonBar(Button... buttons) {
        HBox buttonBar = new HBox(20, buttons);
        buttonBar.setAlignment(Pos.CENTER);
        return buttonBar;
    }

    private static List<BookDetails> getAllBooksSortedByGenre() {
        List<BookDetails> allBooksSortedByGenre = new ArrayList<>();
        // Query the database to retrieve all books and sort them by genre
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT * FROM BOOK ORDER BY GENRE";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        BookDetails book = new BookDetails();
                        book.setId(resultSet.getInt("ID_BOOK"));
                        book.setName(resultSet.getString("NAME"));
                        book.setAuthor(resultSet.getString("AUTHOR"));
                        book.setSynopsis(resultSet.getString("SYNOPSIS"));
                        book.setGenre(resultSet.getString("GENRE"));
                        book.setPicture(resultSet.getBlob("PICTURE"));
                        book.setAvailability(resultSet.getBoolean("AVAILABILITY"));
                        book.setPrice(resultSet.getInt("PRICE"));
                        allBooksSortedByGenre.add(book);
                    }
                }
            }
            System.out.println("All books sorted by genre retrieved successfully."); // Print statement after successful retrieval
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooksSortedByGenre;
    }

    private static List<BookDetails> getAllBooksSortedByRating() {
        List<BookDetails> allBooksSortedByRating = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT BOOK.*, AVG(RATING) AS AVG_RATING " +
                    "FROM BOOK " +
                    "LEFT JOIN RATING ON BOOK.ID_BOOK = RATING.ID_BOOK " +
                    "GROUP BY BOOK.ID_BOOK " +
                    "ORDER BY AVG_RATING DESC";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        BookDetails book = createBookDetails(resultSet);
                        allBooksSortedByRating.add(book);
                    }
                }
            }
            System.out.println("All books sorted by rating retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooksSortedByRating;
    }

    private static void displayBooks(Stage primaryStage, String loggedInUsername, List<BookDetails> books) {
        // Text message
        Text recommendedMessage = new Text("Books By Genre");
        recommendedMessage.setFill(Color.rgb(103, 49, 71));
        recommendedMessage.getStyleClass().add("recommended-message");

        // Buttons
        Button genreButton = new Button("Genre ▼");
        genreButton.getStyleClass().add("genre-button");
        genreButton.setOnAction(event -> {
            // Action for Genre button
            // You can implement the functionality here
            List<BookDetails> allBooksSortedByGenre = getAllBooksSortedByGenre();
            displayBooks(primaryStage, loggedInUsername, allBooksSortedByGenre);
        });

        Button ratingButton = new Button("Rating ▼");
        ratingButton.getStyleClass().add("rating-button");
        ratingButton.setOnAction(event -> {
            // Action for Rating button
            List<BookDetails> allBooksSortedByRating = getAllBooksSortedByRating();
            displayBooksByRating(primaryStage, loggedInUsername, allBooksSortedByRating);
        });

        // Button bar
        HBox buttonBar = createButtonBar(genreButton, ratingButton);

        // Navigation bar
        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, loggedInUsername);

        // Layout setup
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");

        // Add text message and button bar to layout
        layout.getChildren().addAll(recommendedMessage, buttonBar);

        // Group books by genre
        Map<String, List<BookDetails>> booksByGenre = new HashMap<>();
        for (BookDetails book : books) {
            String genre = book.getGenre();
            if (!booksByGenre.containsKey(genre)) {
                booksByGenre.put(genre, new ArrayList<>());
            }
            booksByGenre.get(genre).add(book);
        }

        // Iterate over each genre and add its title and books
        for (Map.Entry<String, List<BookDetails>> entry : booksByGenre.entrySet()) {
            String genre = entry.getKey();
            List<BookDetails> genreBooks = entry.getValue();

            // Genre title
            Text genreTitle = new Text(genre);
            genreTitle.setFill(Color.rgb(103, 49, 71));
            genreTitle.getStyleClass().add("genre-title");
            layout.getChildren().add(genreTitle);

            // Create a VBox to hold the book items of this genre
            VBox genreBookItems = new VBox(10);
            genreBookItems.setAlignment(Pos.TOP_CENTER);

            // Populate the VBox with book items
            for (BookDetails book : genreBooks) {
                VBox bookItemBox = createBookItemBox(book, primaryStage, loggedInUsername);
                genreBookItems.getChildren().add(bookItemBox);
            }

            // Add the VBox for books of this genre to the layout
            layout.getChildren().add(genreBookItems);
        }

        // Create a ScrollPane and set its content to the layout
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add the ScrollPane and navigation bar to a VBox
        VBox contentBox = new VBox(scrollPane, navigationBar);

        // Create the BorderPane for the scene
        BorderPane root = new BorderPane(contentBox);

        // Get the current scene and update it
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
    }


    private static void displayBooksByRating(Stage primaryStage, String loggedInUsername, List<BookDetails> books) {
        // Text message
        Text recommendedMessage = new Text("Books By Rating");
        recommendedMessage.setFill(Color.rgb(103, 49, 71));
        recommendedMessage.getStyleClass().add("recommended-message");

        // Buttons
        Button genreButton = new Button("Genre ▼");
        genreButton.getStyleClass().add("genre-button");
        genreButton.setOnAction(event -> {
            // Action for Genre button
            // You can implement the functionality here
            List<BookDetails> allBooksSortedByGenre = getAllBooksSortedByGenre();
            displayBooks(primaryStage, loggedInUsername, allBooksSortedByGenre);
        });

        Button ratingButton = new Button("Rating ▼");
        ratingButton.getStyleClass().add("rating-button");
        ratingButton.setOnAction(event -> {
            // Action for Rating button
            List<BookDetails> allBooksSortedByRating = getAllBooksSortedByRating();
            displayBooksByRating(primaryStage, loggedInUsername, allBooksSortedByRating);
        });

        // Button bar
        HBox buttonBar = createButtonBar(genreButton, ratingButton);

        // Navigation bar
        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, loggedInUsername);

        // Layout setup
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");

        // Add text message and button bar to layout
        layout.getChildren().addAll(recommendedMessage, buttonBar);

        // Create a VBox to hold the book items sorted by rating
        VBox ratingBookItems = new VBox(10);
        ratingBookItems.setAlignment(Pos.TOP_CENTER);

        // Populate the VBox with book items sorted by rating
        for (BookDetails book : books) {
            VBox bookItemBox = createBookItemBox(book, primaryStage, loggedInUsername);
            ratingBookItems.getChildren().add(bookItemBox);
        }

        // Add the VBox for books sorted by rating to the layout
        layout.getChildren().add(ratingBookItems);

        // Create a ScrollPane and set its content to the layout
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Add the ScrollPane and navigation bar to a VBox
        VBox contentBox = new VBox(scrollPane, navigationBar);

        // Create the BorderPane for the scene
        BorderPane root = new BorderPane(contentBox);

        // Get the current scene and update it
        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
    }

    private static VBox createBookItemBox(BookDetails book, Stage primaryStage, String loggedInUsername) {
        // Create an HBox to hold the book image and its details
        HBox bookItemBox = new HBox(10);
        bookItemBox.setAlignment(Pos.TOP_LEFT);
        bookItemBox.setPadding(new Insets(10));
        bookItemBox.getStyleClass().add("book-item-box");

        // Load book image
        Blob pictureBlob = book.getPicture();
        byte[] pictureBytes = new byte[0];
        try {
            int blobLength = (int) pictureBlob.length();
            pictureBytes = pictureBlob.getBytes(1, blobLength);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ImageView bookImageView = new ImageView(new Image(new ByteArrayInputStream(pictureBytes)));

        bookImageView.setFitWidth(80); // Set the desired width
        bookImageView.setFitHeight(120); // Set the desired height
        bookImageView.setPreserveRatio(true);
        bookImageView.getStyleClass().add("image-view");

        bookItemBox.setOnMouseClicked(event -> {
            BookDescriptionScene.openBookDescriptionScene(primaryStage, book.getId());
        });

        // Create UI elements for book details
        VBox bookDetails = new VBox(5);
        bookDetails.setAlignment(Pos.TOP_LEFT);
        bookDetails.getStyleClass().add("book-details");
        bookDetails.getChildren().addAll(
                new Text("Book Name: " + book.getName()),
                new Text("Author: " + book.getAuthor()),
                new Text("Price: $" + book.getPrice()),
                new Text("Rating: " + calculateBookRating(book.getId()))
        );

        // Add book image and details to the item box
        bookItemBox.getChildren().addAll(bookImageView, bookDetails);

        // Add event handler to open BookDescriptionScene when clicked on the image
        bookImageView.setOnMouseClicked(event -> {
            // Open BookDescriptionScene
            BookDescriptionScene.openBookDescriptionScene(primaryStage, book.getId());
        });

        return new VBox(bookItemBox);
    }
    private static BookDetails createBookDetails(ResultSet resultSet) throws SQLException {
        BookDetails book = new BookDetails();
        book.setId(resultSet.getInt("ID_BOOK"));
        book.setName(resultSet.getString("NAME"));
        book.setAuthor(resultSet.getString("AUTHOR"));
        book.setSynopsis(resultSet.getString("SYNOPSIS"));
        book.setGenre(resultSet.getString("GENRE"));
        book.setPicture(resultSet.getBlob("PICTURE"));
        book.setAvailability(resultSet.getBoolean("AVAILABILITY"));
        book.setPrice(resultSet.getInt("PRICE"));
        return book;
    }

    public static double calculateBookRating(int bookId) {
        double rating = 0.0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT AVG(RATING) AS AVG_RATING FROM RATING WHERE ID_BOOK = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Get the average rating from the result set
                        rating = resultSet.getDouble("AVG_RATING");
                        // If the rating is not null, set it to the calculated value
                        if (!resultSet.wasNull()) {
                            rating = resultSet.getDouble("AVG_RATING");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rating;
    }
}


