package com.example.projekatse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;


public class RentBookWindow {

    /**
     * This function is used when user wants to
     * rent a book that he is viewing in book description scene.
     * It opens new window that displays all information adn input
     * needed to rent a book.
     * @param primaryStage
     * @param bookDetails
     */
    public static void rentBook(Stage primaryStage, BookDetails bookDetails) {
        Stage rentStage = new Stage();
        rentStage.initModality(Modality.APPLICATION_MODAL);
        rentStage.initOwner(primaryStage);
        rentStage.setTitle("Renting");

        VBox rentLayout = new VBox(20);
        rentLayout.setAlignment(Pos.CENTER);
        rentLayout.setPadding(new Insets(20));

        Text rentText = new Text("RENTING");
        rentText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Text bookInfo = new Text(String.format("%s | %s | %s | %d$", bookDetails.getName(), bookDetails.getAuthor(),
                bookDetails.isAvailability() ? "Available" : "Not Available", bookDetails.getPrice()));
        bookInfo.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox cardNumberBox = new HBox(10);
        Label cardNumberLabel = new Label("Card Number:");
        TextField cardNumberField = new TextField();
        cardNumberBox.getChildren().addAll(cardNumberLabel, cardNumberField);

        HBox cvvBox = new HBox(10);
        Label cvvLabel = new Label("CVV:");
        TextField cvvField = new TextField();
        cvvBox.getChildren().addAll(cvvLabel, cvvField);

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Select Due Date");

        CheckBox termsCheckbox = new CheckBox("I agree to the terms of service");

        Button rentNowButton = new Button("Rent Now");
        rentNowButton.setOnAction(event -> {
            if (InputValidator.isValidCardNumber(cardNumberField.getText()) && InputValidator.isValidCVV(cvvField.getText())) {
                if (termsCheckbox.isSelected()) {
                    int userId = LoggedInScene.getLoggedInUserId();
                    System.out.println("ID_USER: " + userId);
                    LocalDate selectedDueDate = dueDatePicker.getValue();
                    if (selectedDueDate != null && selectedDueDate.isAfter(LocalDate.now())) {
                        Database.insertRentDetails(userId, bookDetails.getId(), cardNumberField.getText(), Integer.parseInt(cvvField.getText()), Date.valueOf(selectedDueDate));
                        rentStage.close();
                    } else {
                        InputValidator.showErrorDialog("Please select a valid due date from today onwards.");
                    }
                } else {
                    InputValidator.showErrorDialog("Please agree to the terms of service.");
                }
            } else {
                InputValidator.showErrorDialog("Invalid card number or CVV.");
            }
        });

        rentLayout.getChildren().addAll(rentText, bookInfo, cardNumberBox, cvvBox, dueDatePicker, termsCheckbox, rentNowButton);

        Scene rentScene = new Scene(rentLayout, 400, 350);
        rentScene.getStylesheets().add(BookDescriptionScene.class.getResource("stylerent.css").toExternalForm());
        rentStage.setScene(rentScene);
        rentStage.show();
    }
}
