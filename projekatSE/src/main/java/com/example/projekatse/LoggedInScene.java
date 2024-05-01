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

    /**
     * This function presents a scene when user is logged in succesfully. It contains
     * text as well as search bar that implements search function from another method in this class.
     * It also contains navigation bar.
     * @param primaryStage
     * @param username
     * @param userId
     */
    public static void openLoggedInScene(Stage primaryStage, String username, int userId) {

        Text welcomeMessage = new Text("Welcome to the BorrowedPages!\nYou can search your desired book in the search bar.");
        welcomeMessage.setFill(Color.rgb(103, 49, 71));
        welcomeMessage.getStyleClass().add("welcome-message");

        loggedInUserId = userId;

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

        HBox navigationBar = createNavigationBar(primaryStage, username);

        VBox layout = new VBox(20, welcomeMessage, searchBar);
        layout.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane(layout);
        root.setId("loggedInScene");
        root.getStyleClass().add("root");
        root.setBottom(navigationBar);

        Scene loggedInScene = new Scene(root, 600, 600);
        loggedInScene.getStylesheets().add(LoggedInScene.class.getResource("styleloggedin.css").toExternalForm());
        primaryStage.setScene(loggedInScene);

        BookDescriptionScene.setPrimaryStage(primaryStage);
        loggedInUserId = Database.getUserId(username);
    }

    /**
     * This method is used for creation of navigation bar at bottom of every scene.
     * It has three buttons that when clikced lead to different scenes. Search button leads to openLoggedInScene
     * that has search bar. Recommended button leads to RecommendedBookScene and history button leads to HistoryRentScene.
     * @param primaryStage
     * @param username
     * @return
     */
    static HBox createNavigationBar(Stage primaryStage, String username) {
        Button searchButton = new Button();
        searchButton.getStyleClass().add("navigation-button");
        searchButton.setId("search-button");
        searchButton.setOnAction(event -> {
            String loggedInUsername = LoginInterface.getLoggedInUsername();
            int userId = Database.getUserId(loggedInUsername);
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

    /**
     * This method is implemented in search bar in openLoggedInScene. It takes search text that is put
     * in search bar fields and matches it name with id of a book in database and retrives it.
     * @param primaryStage
     * @param searchQuery
     */
    private static void searchBook(Stage primaryStage, String searchQuery) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root",
                "veljko1503")) {
            String query = "SELECT ID_BOOK FROM BOOK WHERE NAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, searchQuery);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int bookId = resultSet.getInt("ID_BOOK");
                        BookDescriptionScene.openBookDescriptionScene(primaryStage, bookId);
                    } else {
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









