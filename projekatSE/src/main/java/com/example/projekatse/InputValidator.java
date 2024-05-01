package com.example.projekatse;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InputValidator {
    /**
     * This function checks if the name is valid using regex.
     * @param name
     * @return
     */
    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    /**
     * This function checks if the age input is valid.
     * @param age
     * @return
     */
    public static boolean isValidAge(String age) {
        int ageInt;
        try {
            ageInt = Integer.parseInt(age);
            return ageInt >= 16 && ageInt <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * This function checks if card number entered is valid.
     * @param cardNumber
     * @return
     */
    public static boolean isValidCardNumber(String cardNumber) {
        return cardNumber.length() == 16 && cardNumber.matches("\\d+");
    }

    /**
     * This functions checks if cvv input is valid.
     * @param cvv
     * @return
     */
    public static boolean isValidCVV(String cvv) {
        return cvv.length() == 3 && cvv.matches("\\d+");
    }

    /**
     * This function checks if username and password are right lenght.
     * @param username
     * @param password
     * @return
     */
    public static boolean isValidUsernameAndPassword(String username, String password) {
        return username.length() > 6 && password.length() > 6;
    }

    /**
     * This function is used when we want to display that user had error
     * while inputing something.
     * @param message
     */
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

