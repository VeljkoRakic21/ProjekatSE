package com.example.projekatse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RecommendedBookScene {
    private static String genre;

    /**
     * This function is used to create a scene for recommended books by genre or rating and displays them.
     * @param primaryStage
     * @param username
     */
    public static void openRecommendedBookScene(Stage primaryStage, String username) {
        Text recommendedMessage = new Text("Recommended Books");
        recommendedMessage.setFill(Color.rgb(103, 49, 71));
        recommendedMessage.getStyleClass().add("recommended-message");

        Button genreButton = new Button("Genre ▼");
        genreButton.getStyleClass().add("genre-button");
        genreButton.setOnAction(event -> {
            List<BookDetails> allBooksSortedByGenre = getAllBooksSortedByGenre();
            displayBooks(primaryStage, username, allBooksSortedByGenre);
        });

        Button ratingButton = new Button("Rating ▼");
        ratingButton.getStyleClass().add("rating-button");
        ratingButton.setOnAction(event -> {
            List<BookDetails> allBooksSortedByRating = getAllBooksSortedByRating();
            displayBooksByRating(primaryStage, username, allBooksSortedByRating);
        });

        Button videoButton = new Button("Mystery Video ▼");
        videoButton.setOnAction(event -> playYouTubeVideo(primaryStage, "https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
        videoButton.getStyleClass().add("rating-button");

        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, username);

        VBox layout = new VBox(20, recommendedMessage, createButtonBar(genreButton, ratingButton, videoButton));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");
        BorderPane root = new BorderPane(layout);
        root.setBottom(navigationBar);

        Scene recommendedBookScene = new Scene(root, 600, 600);
        recommendedBookScene.getStylesheets().add(RecommendedBookScene.class.getResource("stylerecommended.css").toExternalForm());
        primaryStage.setScene(recommendedBookScene);
    }

    /**
     * Creates button for genre and rating.
     * @param buttons
     * @return
     */
    private static HBox createButtonBar(Button... buttons) {
        HBox buttonBar = new HBox(20, buttons);
        buttonBar.setAlignment(Pos.CENTER);
        return buttonBar;
    }

    /**
     * This function sorts book by genre by their genre in database and implements it in genre button.
     * @return
     */
    private static List<BookDetails> getAllBooksSortedByGenre() {
        List<BookDetails> allBooksSortedByGenre = new ArrayList<>();
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
            System.out.println("All books sorted by genre retrieved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooksSortedByGenre;
    }

    /**
     * This function sorts books by rating by taking it from rating row in database that can be acitvly changed by
     * user submiting rates.
     * @return
     */
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

    /**
     * This function is used for displaying books by genre using maps and lists.
     * @param primaryStage
     * @param loggedInUsername
     * @param books
     */
    private static void displayBooks(Stage primaryStage, String loggedInUsername, List<BookDetails> books) {
        Text recommendedMessage = new Text("Books By Genre");
        recommendedMessage.setFill(Color.rgb(103, 49, 71));
        recommendedMessage.getStyleClass().add("recommended-message");

        Button genreButton = new Button("Genre ▼");
        genreButton.getStyleClass().add("genre-button");
        genreButton.setOnAction(event -> {
            List<BookDetails> allBooksSortedByGenre = getAllBooksSortedByGenre();
            displayBooks(primaryStage, loggedInUsername, allBooksSortedByGenre);
        });

        Button ratingButton = new Button("Rating ▼");
        ratingButton.getStyleClass().add("rating-button");
        ratingButton.setOnAction(event -> {
            List<BookDetails> allBooksSortedByRating = getAllBooksSortedByRating();
            displayBooksByRating(primaryStage, loggedInUsername, allBooksSortedByRating);
        });

        HBox buttonBar = createButtonBar(genreButton, ratingButton);

        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, loggedInUsername);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");

        layout.getChildren().addAll(recommendedMessage, buttonBar);

        Map<String, List<BookDetails>> booksByGenre = new HashMap<>();
        for (BookDetails book : books) {
            String genre = book.getGenre();
            if (!booksByGenre.containsKey(genre)) {
                booksByGenre.put(genre, new ArrayList<>());
            }
            booksByGenre.get(genre).add(book);
        }

        for (Map.Entry<String, List<BookDetails>> entry : booksByGenre.entrySet()) {
            String genre = entry.getKey();
            List<BookDetails> genreBooks = entry.getValue();

            Text genreTitle = new Text(genre);
            genreTitle.setFill(Color.rgb(103, 49, 71));
            genreTitle.getStyleClass().add("genre-title");
            layout.getChildren().add(genreTitle);

            VBox genreBookItems = new VBox(10);
            genreBookItems.setAlignment(Pos.TOP_CENTER);

            for (BookDetails book : genreBooks) {
                VBox bookItemBox = createBookItemBox(book, primaryStage, loggedInUsername);
                genreBookItems.getChildren().add(bookItemBox);
            }

            layout.getChildren().add(genreBookItems);
        }

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox contentBox = new VBox(scrollPane, navigationBar);

        BorderPane root = new BorderPane(contentBox);

        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
    }

    /**
     * This function is used for displaying books by rating using maps and lists.
     * @param primaryStage
     * @param loggedInUsername
     * @param books
     */
    private static void displayBooksByRating(Stage primaryStage, String loggedInUsername, List<BookDetails> books) {
        Text recommendedMessage = new Text("Books By Rating");
        recommendedMessage.setFill(Color.rgb(103, 49, 71));
        recommendedMessage.getStyleClass().add("recommended-message");

        Button genreButton = new Button("Genre ▼");
        genreButton.getStyleClass().add("genre-button");
        genreButton.setOnAction(event -> {
            List<BookDetails> allBooksSortedByGenre = getAllBooksSortedByGenre();
            displayBooks(primaryStage, loggedInUsername, allBooksSortedByGenre);
        });

        Button ratingButton = new Button("Rating ▼");
        ratingButton.getStyleClass().add("rating-button");
        ratingButton.setOnAction(event -> {
            List<BookDetails> allBooksSortedByRating = getAllBooksSortedByRating();
            displayBooksByRating(primaryStage, loggedInUsername, allBooksSortedByRating);
        });

        HBox buttonBar = createButtonBar(genreButton, ratingButton);

        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, loggedInUsername);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("root");

        layout.getChildren().addAll(recommendedMessage, buttonBar);

        VBox ratingBookItems = new VBox(10);
        ratingBookItems.setAlignment(Pos.TOP_CENTER);

        for (BookDetails book : books) {
            VBox bookItemBox = createBookItemBox(book, primaryStage, loggedInUsername);
            ratingBookItems.getChildren().add(bookItemBox);
        }

        layout.getChildren().add(ratingBookItems);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox contentBox = new VBox(scrollPane, navigationBar);

        BorderPane root = new BorderPane(contentBox);

        Scene scene = primaryStage.getScene();
        scene.setRoot(root);
    }

    /**
     * This function creates item box where book information is being displayed.
     * @param book
     * @param primaryStage
     * @param loggedInUsername
     * @return
     */
    private static VBox createBookItemBox(BookDetails book, Stage primaryStage, String loggedInUsername) {
        HBox bookItemBox = new HBox(10);
        bookItemBox.setAlignment(Pos.TOP_LEFT);
        bookItemBox.setPadding(new Insets(10));
        bookItemBox.getStyleClass().add("book-item-box");

        Blob pictureBlob = book.getPicture();
        byte[] pictureBytes = new byte[0];
        try {
            int blobLength = (int) pictureBlob.length();
            pictureBytes = pictureBlob.getBytes(1, blobLength);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ImageView bookImageView = new ImageView(new Image(new ByteArrayInputStream(pictureBytes)));

        bookImageView.setFitWidth(80);
        bookImageView.setFitHeight(120);
        bookImageView.setPreserveRatio(true);
        bookImageView.getStyleClass().add("image-view");

        bookItemBox.setOnMouseClicked(event -> {
            BookDescriptionScene.openBookDescriptionScene(primaryStage, book.getId());
        });

        VBox bookDetails = new VBox(5);
        bookDetails.setAlignment(Pos.TOP_LEFT);
        bookDetails.getStyleClass().add("book-details");
        bookDetails.getChildren().addAll(
                new Text("Book Name: " + book.getName()),
                new Text("Author: " + book.getAuthor()),
                new Text("Price: $" + book.getPrice()),
                new Text("Rating: " + calculateBookRating(book.getId()))
        );

        bookItemBox.getChildren().addAll(bookImageView, bookDetails);

        bookImageView.setOnMouseClicked(event -> {
            BookDescriptionScene.openBookDescriptionScene(primaryStage, book.getId());
        });

        return new VBox(bookItemBox);
    }

    /**
     *This function retrives book information from the Result Set that is passed to it and it constructs BookDetails for each
     * book being displayed.
     * @param resultSet
     * @return
     * @throws SQLException
     */
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

    /**
     * This function is used for calculating book ratings.
     * @param bookId
     * @return
     */
    public static double calculateBookRating(int bookId) {
        double rating = 0.0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT AVG(RATING) AS AVG_RATING FROM RATING WHERE ID_BOOK = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        rating = resultSet.getDouble("AVG_RATING");
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

    private static void playYouTubeVideo(Stage primaryStage, String videoUrl) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(videoUrl);

        VBox root = new VBox(webView);
        Scene scene = new Scene(root, 500, 400);

        Stage newStage = new Stage();
        newStage.setTitle("WebView");
        newStage.setOnCloseRequest(event -> webEngine.load("about:blank"));
        newStage.setScene(scene);
        newStage.show();
    }
}


