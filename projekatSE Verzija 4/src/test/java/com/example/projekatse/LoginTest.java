package com.example.projekatse;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage); // Main is your JavaFX application class
    }

    @Test
    public void testSuccessfulLogin() {
        // First, click the "Log-in" button on the main screen to bring up the login interface
        clickOn("Log-in");

        // Then, click on the username field and enter the username
        clickOn("#usernameTextField").write("veljko1503");

        // Click on the password field and enter the password
        clickOn("#passwordTextField").write("veljko1503");

        // Click on the login button in the login interface
        clickOn("#loginButton");

        sleep(2000);

        // Check for a condition that represents a successful login
        // For example, if a successful login changes the scene to one with an ID "loggedInScene", check for its presence
        assertTrue(lookup("#loggedInScene").tryQuery().isPresent(), "Login was not successful.");
    }
}

