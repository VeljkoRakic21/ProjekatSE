package com.example.projekatse;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggedInScene {
    private static final Logger logger = LoggerFactory.getLogger(LoggedInScene.class);
    private static int loggedInUserId;

    public static void openLoggedInScene(Stage primaryStage, String username, int userId) {
        // Welcome message
        Text welcomeMessage = new Text("Welcome to the BorrowedPages!\nYou can search your desired book in the search bar.");
        welcomeMessage.setFill(Color.rgb(103, 49, 71));
        welcomeMessage.getStyleClass().add("welcome-message");

        // Set the loggedInUserId
        loggedInUserId = userId;


        // Search bar
        TextField searchBar = new TextField();
        searchBar.setId("searchBar");
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(500);
        searchBar.setMaxWidth(500);
        searchBar.setPrefHeight(40);
        searchBar.setMaxHeight(40);
        searchBar.getStyleClass().add("search-bar");
        searchBar.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchBook(primaryStage, searchBar.getText());
            }
        });

        // Navigation bar
        HBox navigationBar = createNavigationBar(primaryStage, username);

        // Layout setup
        VBox layout = new VBox(20, welcomeMessage, searchBar);
        layout.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(layout);
        root.setId("loggedInScene");
        root.getStyleClass().add("root");
        root.setBottom(navigationBar);

        // Create the scene
        Scene loggedInScene = new Scene(root, 600, 600);
        loggedInScene.getStylesheets().add(LoggedInScene.class.getResource("styleloggedin.css").toExternalForm());
        primaryStage.setScene(loggedInScene);

        // If the user directly navigates to the BookDescriptionScene, ensure the primaryStage is set
        BookDescriptionScene.setPrimaryStage(primaryStage);
        loggedInUserId = Database.getUserId(username);
    }

    static HBox createNavigationBar(Stage primaryStage, String username) {
        Button searchButton = new Button();
        searchButton.getStyleClass().add("navigation-button");
        searchButton.setId("search-button");
        searchButton.setOnAction(event -> {
            // Retrieve the logged-in username
            String loggedInUsername = LoginInterface.getLoggedInUsername();
            // Pass the username to the getUserId method to retrieve the user ID
            int userId = Database.getUserId(loggedInUsername);
            // Open LoggedInScene with the retrieved user ID
            openLoggedInScene(primaryStage, loggedInUsername, userId);
        });

        Button recommendedButton = new Button();
        recommendedButton.getStyleClass().add("navigation-button");
        recommendedButton.setId("recommended-button");
        recommendedButton.setOnAction(event -> {
            String loggedInUsername = LoginInterface.getLoggedInUsername();
            RecommendedBookScene.openRecommendedBookScene(primaryStage, loggedInUsername);
        });

        Button historyButton = new Button();
        historyButton.getStyleClass().add("navigation-button");
        historyButton.setId("history-button");
        historyButton.setOnAction(event -> {
            String loggedInUsername = LoginInterface.getLoggedInUsername();
            HistoryRentScene.openHistoryRentScene(primaryStage, loggedInUsername);
        });

        HBox navigationBar = new HBox(searchButton, recommendedButton, historyButton);
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.getStyleClass().add("navigation-bar");

        return navigationBar;
    }

    private static void searchBook(Stage primaryStage, String searchQuery) {
        // Query the database to check if the search query matches any book names
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root",
                "veljko1503")) {
            String query = "SELECT ID_BOOK FROM BOOK WHERE NAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, searchQuery);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int bookId = resultSet.getInt("ID_BOOK");
                        // Open BookDescriptionScene with the details of the matched book
                        BookDescriptionScene.openBookDescriptionScene(primaryStage, bookId);
                    } else {
                        // Show a message if no matching book is found
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("No book found with the exact name matching your search query.");
                        Pane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(Database.class.getResource("stylealert.css").toExternalForm());
                        alert.showAndWait();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to search for book: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }
}









