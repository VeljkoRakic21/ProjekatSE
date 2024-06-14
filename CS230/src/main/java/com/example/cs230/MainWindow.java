package com.example.cs230;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class MainWindow {

    @FXML
    private Button uploadButton;

    @FXML
    private Button downloadButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button viewButton;

    @FXML
    private ListView<String> fileList;

    private Stage stage;
    private Orchestrator orchestrator;
    private String token;

    @FXML
    public void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            AccessControlSettings settings = showAccessControlDialog(file);

            if (settings != null) {
                boolean success = orchestrator.uploadFile(token, file, "private", settings);

                if (success) {
                    System.out.println("File uploaded successfully");
                } else {
                    System.out.println("Failed to upload file");
                }
            }
        }
    }

    @FXML
    public void handleDownload() {
        String selectedFile = fileList.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Download File");
            fileChooser.setInitialFileName(selectedFile);
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                File downloadedFile = orchestrator.downloadFile(token, selectedFile);

                if (downloadedFile != null) {
                    try {
                        byte[] fileData = Files.readAllBytes(downloadedFile.toPath());
                        Files.write(file.toPath(), fileData);
                        System.out.println("Download successful");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Failed to download file");
                }
            }
        } else {
            System.out.println("Please select a file to download");
        }
    }

    @FXML
    public void handleDelete() {
        String selectedFile = fileList.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            boolean success = orchestrator.deleteFile(token, selectedFile);

            if (success) {
                fileList.getItems().remove(selectedFile);
                System.out.println("Delete successful");
            } else {
                System.out.println("Failed to delete file");
            }
        } else {
            System.out.println("Please select a file to delete");
        }
    }

    @FXML
    public void handleView() {
        String[] files = orchestrator.listFiles(token);

        if (files != null) {
            fileList.getItems().clear();
            fileList.getItems().addAll(Arrays.asList(files));
            System.out.println("Files listed successfully");
        } else {
            System.out.println("Failed to list files");
        }
    }

    private AccessControlSettings showAccessControlDialog(File file) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cs230/AccessControlDialog.fxml"));
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Access Control Settings");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AccessControlDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                return controller.getAccessControlSettings();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOrchestrator(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
