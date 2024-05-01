package com.example.projekatse;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;

import static com.example.projekatse.RecommendedBookScene.calculateBookRating;

public class BookDescriptionScene {
    private static final Logger logger = LoggerFactory.getLogger(BookDescriptionScene.class);
    private static Stage primaryStage;
    private static String username;

    /**
     * Displays all contet about book that is either being searched or clicked from recommended scenes.
     * It gets all information from data class BookDetails and then puts them in built UI. It also implements
     * renting button that leads to window for renting.
     * @param primaryStage
     * @param id
     */
    public static void openBookDescriptionScene(Stage primaryStage, int id) {
        BookDetails bookDetails = Database.getBookDetails(id);

        String bookName = bookDetails.getName();
        String authorName = bookDetails.getAuthor();
        String synopsis = bookDetails.getSynopsis();
        String genre = bookDetails.getGenre();
        Blob pictureBlob = bookDetails.getPicture();

        Image bookCoverImage = null;
        try {
            if (pictureBlob != null && pictureBlob.length() > 0) {
                bookCoverImage = new Image(pictureBlob.getBinaryStream());
            }
        } catch (SQLException e) {
            logger.error("Failed to load book cover image: {}", e.getMessage());
            e.printStackTrace();
        }

        Text bookTitle = new Text(bookName);
        bookTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bookTitle.setId("bookTitle");

        Text author = new Text("Author: " + authorName);
        author.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text genreText = new Text("Genre: " + genre);
        genreText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label availabilityLabel = new Label();
        availabilityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        availabilityLabel.setText(bookDetails.isAvailability() ? "Available" : "Not Available");

        Label priceLabel = new Label();
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        priceLabel.setText(String.format("%d$", bookDetails.getPrice()));

        Label ratingLabel = new Label();
        ratingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ratingLabel.setText(String.format("%.1f/5", calculateBookRating(bookDetails.getId())));

        HBox availabilityPriceRatingBox = new HBox(20);
        availabilityPriceRatingBox.setAlignment(Pos.CENTER);
        availabilityPriceRatingBox.getChildren().addAll(availabilityLabel,new Text(" | "), priceLabel, new Text(" | "), ratingLabel);

        Text synopsisText = new Text("Synopsis:\n" + synopsis);
        synopsisText.setFont(Font.font("Arial", 14));
        synopsisText.setWrappingWidth(200);

        Rectangle bookCover = new Rectangle(200, 250);
        bookCover.setFill(Color.LIGHTGRAY);
        if (bookCoverImage != null) {
            ImageView imageView = new ImageView(bookCoverImage);
            imageView.setFitWidth(200);
            imageView.setFitHeight(250);
            bookCover = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
            bookCover.setFill(new ImagePattern(bookCoverImage));
        }

        Button rentButton = new Button("Rent");
        rentButton.getStyleClass().add("rent-button");
        rentButton.setOnAction(event -> RentBookWindow.rentBook(primaryStage, bookDetails));

        HBox navigationBar = LoggedInScene.createNavigationBar(primaryStage, username);

        VBox.setMargin(bookTitle, new Insets(18, 0, 0, 0));
        VBox.setMargin(rentButton, new Insets(0, 0, 20, 0));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(bookTitle, author, genreText, bookCover, availabilityPriceRatingBox, createSynopsisBox(synopsisText), rentButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: #e5d2b8;");

        BorderPane root = new BorderPane(scrollPane);
        root.setBottom(navigationBar);

        layout.setStyle("-fx-background-color: #e5d2b8;");

        Scene bookDescriptionScene = new Scene(root, 600, 600);
        bookDescriptionScene.getStylesheets().add(BookDescriptionScene.class.getResource("stylebook.css").toExternalForm());
        primaryStage.setScene(bookDescriptionScene);

        Platform.runLater(() -> scrollPane.setVvalue(0));
    }

    /**
     * Sets BookDescriptionScene as primary stage.
     * @param stage
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Creates box that contains synopsis for book that is being displayed.
     * @param synopsisText
     * @return
     */
    private static VBox createSynopsisBox(Text synopsisText) {
        VBox synopsisBox = new VBox();
        synopsisBox.setPrefSize(50, 50);
        synopsisBox.getStyleClass().add("synopsis-box");
        synopsisBox.setAlignment(Pos.CENTER);
        synopsisBox.getChildren().add(synopsisText);
        return synopsisBox;
    }
}

