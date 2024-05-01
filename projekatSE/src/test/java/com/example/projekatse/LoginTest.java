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
        new Main().start(stage);
    }

    @Test
    public void testSuccessfulLogin() {

        clickOn("Log-in");

        clickOn("#usernameTextField").write("veljko1503");

        clickOn("#passwordTextField").write("veljko1503");

        clickOn("#loginButton");

        sleep(2000);

        assertTrue(lookup("#loggedInScene").tryQuery().isPresent(), "Login was not successful.");
    }
}

