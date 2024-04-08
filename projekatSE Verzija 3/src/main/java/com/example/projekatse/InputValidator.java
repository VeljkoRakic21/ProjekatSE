package com.example.projekatse;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;

import java.util.regex.Pattern;

public class InputValidator {
    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public static boolean isValidAge(String age) {
        int ageInt;
        try {
            ageInt = Integer.parseInt(age);
            return ageInt >= 16 && ageInt <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidCardNumber(String cardNumber) {
        return cardNumber.length() == 16 && cardNumber.matches("\\d+");
    }

    public static boolean isValidCVV(String cvv) {
        return cvv.length() == 3 && cvv.matches("\\d+");
    }

    public static boolean isValidUsernameAndPassword(String username, String password) {
        return username.length() > 6 && password.length() > 6;
    }

    public static void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(InputValidator.class.getResource("stylealert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myAlert");
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: white;");
        alert.showAndWait();
    }
}

