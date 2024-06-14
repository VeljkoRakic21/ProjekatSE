package com.example.cs230;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage stage;
    private Orchestrator orchestrator;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) {
            System.out.println("Login successful!");
            String token = JwtUtil.generateToken(username); // Generisanje tokena
            loadMainWindow(token);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password_hash = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void loadMainWindow(String token) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs230/mainw.fxml"));
            Parent root = loader.load();

            MainWindow controller = loader.getController();
            controller.setStage(stage);
            controller.setToken(token);
            controller.setOrchestrator(orchestrator);

            stage.setScene(new Scene(root));
            stage.setTitle("Main Window");
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load mainw.fxml:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOrchestrator(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }
}
