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

    /**
     * This contains Main Scene that welcomes user
     * when application is started.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BorrowedPages");


        Button loginButton = new Button("Log-in");
        Button registerButton = new Button("Register");


        loginButton.setOnAction(e -> replaceWithLoginInterface(primaryStage));
        registerButton.setOnAction(e -> new openRegisterWindow().open());



        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(loginButton, registerButton);
        hbox.setPadding(new Insets(20));


        hbox.setAlignment(Pos.CENTER);


        Text label = new Text("Welcome to\nBorrowedPages");
        label.setFill(Color.rgb(103, 49, 71));
        label.setFont(Font.font("", FontWeight.BOLD, 50));


        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(label, hbox);
        vbox.setAlignment(Pos.CENTER);


        StackPane root = new StackPane();
        root.getChildren().add(vbox);


        Scene scene = new Scene(root, 600, 600);
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);


        primaryStage.show();
    }

    /**
     * This function replaces Main Scene with Log-in interface
     * when loginButton is clicked.
     * @param primaryStage
     */
    private void replaceWithLoginInterface(Stage primaryStage) {
        Button loginButton = new Button("Log-in");
        loginButton.setOnAction(e -> replaceWithLoginInterface(primaryStage));

        VBox loginVBox = LoginInterface.createLoginInterface(primaryStage);
        ((VBox)((StackPane)primaryStage.getScene().getRoot()).getChildren().getFirst()).getChildren().set(1, loginVBox);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

