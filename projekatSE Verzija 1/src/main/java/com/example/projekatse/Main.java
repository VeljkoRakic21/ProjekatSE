package com.example.projekatse;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BorrowedPages");

        // Create the buttons
        Button loginButton = new Button("Log-in");
        Button registerButton = new Button("Register");

        // Set the action for each button
        loginButton.setOnAction(e -> replaceWithLoginInterface(primaryStage));
        registerButton.setOnAction(e -> new openRegisterWindow().open());


        // Create a horizontal layout for the buttons
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(loginButton, registerButton);
        hbox.setPadding(new Insets(20));

        // Centrirajte dugmad u HBox kontejneru
        hbox.setAlignment(Pos.CENTER);

        // Create a label with the text "Dobrodosli u RENTaBOOK"
        Text label = new Text("Welcome to\nBorrowedPages");
        label.setFill(Color.rgb(103, 49, 71));
        label.setFont(Font.font("", FontWeight.BOLD, 50));

        // Create a vertical layout for the label and buttons
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(label, hbox);
        vbox.setAlignment(Pos.CENTER);

        // Create a stack pane to hold the layout
        StackPane root = new StackPane();
        root.getChildren().add(vbox);

        // Set the scene
        Scene scene = new Scene(root, 600, 600);
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void replaceWithLoginInterface(Stage primaryStage) {
        // Initialize the loginButton within this method
        Button loginButton = new Button("Log-in");
        loginButton.setOnAction(e -> replaceWithLoginInterface(primaryStage));

        VBox loginVBox = LoginInterface.createLoginInterface(primaryStage);
        ((VBox)((StackPane)primaryStage.getScene().getRoot()).getChildren().get(0)).getChildren().set(1, loginVBox);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

