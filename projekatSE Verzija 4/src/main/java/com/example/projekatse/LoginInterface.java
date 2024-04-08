package com.example.projekatse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.example.projekatse.LoggedInScene.openLoggedInScene;

public class LoginInterface {
    // Static variable to hold the username
    private static String loggedInUsername;

    public static VBox createLoginInterface(Stage primaryStage) {
        // Create the text fields for username and password
        TextField usernameTextField = new TextField();
        usernameTextField.setId("usernameTextField"); // Set the FX ID
        usernameTextField.setPromptText("Username");
        usernameTextField.setPrefWidth(260);
        usernameTextField.setMaxWidth(260);
        usernameTextField.setPrefHeight(40);
        usernameTextField.setMaxHeight(40);

        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setId("passwordTextField"); // Set the FX ID
        passwordTextField.setPromptText("Password");
        passwordTextField.setPrefWidth(260);
        passwordTextField.setMaxWidth(260);
        passwordTextField.setPrefHeight(40);
        passwordTextField.setMaxHeight(40);

        Button loginButton = new Button("Log in");
        loginButton.setId("loginButton"); // Set the FX ID
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            // Authenticate user using database
            boolean authenticated = Database.authenticateUser(username, password);
            if (authenticated) {
                int userId = Database.getUserId(username); // Retrieve user ID
                // Set the loggedInUsername variable
                loggedInUsername = username;
                openLoggedInScene(primaryStage, username, userId);// Pass user ID to LoggedInScene
            } else {
                // Display error message or handle authentication failure
                InputValidator.showErrorDialog("Invalid username or password.");
            }
        });

        // Create a vertical layout for the login interface
        VBox loginVBox = new VBox(10);
        loginVBox.getChildren().addAll(usernameTextField, passwordTextField, loginButton);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setPadding(new Insets(20));

        return loginVBox;
    }

    // Getter method for the loggedInUsername variable
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }
}





