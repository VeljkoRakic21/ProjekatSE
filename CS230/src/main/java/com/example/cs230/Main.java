package com.example.cs230;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs230/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");

        Login controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        primaryStage.show();

        // Pokrećemo skladišne servere i orkestrator
        new Thread(() -> {
            try {
                StorageServer server1 = new StorageServer("server1");
                StorageServer server2 = new StorageServer("server2");
                StorageServer server3 = new StorageServer("server3");

                Database database = new Database();

                Orchestrator orchestrator = new Orchestrator(List.of(server1, server2, server3), database);
                controller.setOrchestrator(orchestrator);

                System.out.println("Storage servers and orchestrator are up and running.");
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
