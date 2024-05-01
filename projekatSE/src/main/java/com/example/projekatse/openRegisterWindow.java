package com.example.projekatse;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class openRegisterWindow{

    private Database database;
    private Stage registerStage;
    public static boolean registrationSuccessAlertShown = false;

    public openRegisterWindow() {
        this.database = new Database();
    }

    /**
     * This function builds UI for registering user with implmented logic from database class.
     */
    public void open() {
        registerStage = new Stage();
        registerStage.setTitle("Register");

        Text nameLabel = new Text("Name:");
        nameLabel.setFill(Color.BLACK);
        TextField nameTextField = new TextField();
        nameTextField.setId("nameTextField");

        Text lastNameLabel = new Text("Last name:");
        lastNameLabel.setFill(Color.BLACK);
        TextField lastNameTextField = new TextField();
        lastNameTextField.setId("lastNameTextField");

        Text ageLabel = new Text("Age:");
        ageLabel.setFill(Color.BLACK);
        TextField ageTextField = new TextField();
        ageTextField.setId("ageTextField");

        Text emailLabel = new Text("Email:");
        emailLabel.setFill(Color.BLACK);
        TextField emailTextField = new TextField();
        emailTextField.setId("emailTextField");

        Text addressLabel = new Text("Address:");
        addressLabel.setFill(Color.BLACK);
        TextField addressTextField = new TextField();
        addressTextField.setId("addressTextField");

        Text usernameLabel = new Text("Username:");
        usernameLabel.setFill(Color.BLACK);
        TextField usernameTextField = new TextField();
        usernameTextField.setId("usernameTextField");

        Text passwordLabel = new Text("Password:");
        passwordLabel.setFill(Color.BLACK);
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setId("passwordTextField");

        Button registerNowButton = new Button("Register up");
        registerNowButton.setOnAction(e -> {
            String name = nameTextField.getText();
            String lastName = lastNameTextField.getText();
            String age = ageTextField.getText();
            String email = emailTextField.getText();
            String address = addressTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            if (name.isEmpty() || lastName.isEmpty() || age.isEmpty() || email.isEmpty() || address.isEmpty() || username.isEmpty() || password.isEmpty()) {
                InputValidator.showErrorDialog("Error: All fields must be filled out.");
            } else if (!InputValidator.isValidName(name) || !InputValidator.isValidName(lastName)) {
                InputValidator.showErrorDialog("Error: Name and last name must contain only letters.");
            } else if (!InputValidator.isValidAge(age)) {
                InputValidator.showErrorDialog("Error: Age must be your real age between 16-100 and it must be Integer.");
            } else if (!InputValidator.isValidUsernameAndPassword(username, password)) {
                InputValidator.showErrorDialog("Error: Username and password must be at least 6 characters long.");
            } else {
                Database.registerUser(name, lastName, Integer.parseInt(age), email, address, username, password);
                showRegistrationSuccessAlert();
            }
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(nameLabel, nameTextField, lastNameLabel,
                lastNameTextField, ageLabel, ageTextField, emailLabel, emailTextField,
                addressLabel, addressTextField, usernameLabel, usernameTextField, passwordLabel,
                passwordTextField, registerNowButton);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 600);
        String css2 = this.getClass().getResource("styleregister.css").toExternalForm();
        scene.getStylesheets().add(css2);
        registerStage.setScene(scene);

        registerStage.show();
    }

    /**
     * Shows alert to inform user that his registration was successful.
     */
    private void showRegistrationSuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText("You have successfully registered.");

        successAlert.setOnCloseRequest(event -> registerStage.close());

        String css2 = getClass().getResource("stylealert.css").toExternalForm();
        successAlert.getDialogPane().getStylesheets().add(css2);

        successAlert.setOnCloseRequest(event -> {
            openRegisterWindow.registrationSuccessAlertShown = true;
            registerStage.close();
        });

        successAlert.showAndWait();
    }
}




