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

    private static String loggedInUsername;

    /**
     * This function creates log in interface that user can use to
     * log in to his account by entering username and password in right
     * fields. Also, loginButton has authentification function implemented
     * in it from database class.
     * @param primaryStage
     * @return
     */
    public static VBox createLoginInterface(Stage primaryStage) {

        TextField usernameTextField = new TextField();
        usernameTextField.setId("usernameTextField");
        usernameTextField.setPromptText("Username");
        usernameTextField.setPrefWidth(260);
        usernameTextField.setMaxWidth(260);
        usernameTextField.setPrefHeight(40);
        usernameTextField.setMaxHeight(40);

        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setId("passwordTextField");
        passwordTextField.setPromptText("Password");
        passwordTextField.setPrefWidth(260);
        passwordTextField.setMaxWidth(260);
        passwordTextField.setPrefHeight(40);
        passwordTextField.setMaxHeight(40);

        Button loginButton = new Button("Log in");
        loginButton.setId("loginButton");
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            boolean authenticated = Database.authenticateUser(username, password);
            if (authenticated) {

                int userId = Database.getUserId(username);

                loggedInUsername = username;
                openLoggedInScene(primaryStage, username, userId);
            } else {
                InputValidator.showErrorDialog("Invalid username or password.");
            }
        });

        VBox loginVBox = new VBox(10);
        loginVBox.getChildren().addAll(usernameTextField, passwordTextField, loginButton);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.setPadding(new Insets(20));

        return loginVBox;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }
}





