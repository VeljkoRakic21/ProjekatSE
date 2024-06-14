package com.example.cs230;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class AccessControlDialogController {

    @FXML
    private CheckBox viewCheckBox;

    @FXML
    private CheckBox deleteCheckBox;

    @FXML
    private CheckBox downloadCheckBox;

    private Stage dialogStage;
    private boolean saveClicked = false;
    private AccessControlSettings accessControlSettings;

    @FXML
    private void initialize() {
        accessControlSettings = new AccessControlSettings();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public AccessControlSettings getAccessControlSettings() {
        return accessControlSettings;
    }

    @FXML
    private void handleSave() {
        accessControlSettings.setView(viewCheckBox.isSelected());
        accessControlSettings.setDelete(deleteCheckBox.isSelected());
        accessControlSettings.setDownload(downloadCheckBox.isSelected());
        saveClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
