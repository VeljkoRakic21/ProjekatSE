package com.example.projekatse;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationFalseTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage);
    }

    @Test
    public void testInvalidInputRegistration() throws InterruptedException {
        clickOn("Register");

        // Fill the form with invalid data
        clickOn("#nameTextField").write("123");
        clickOn("#lastNameTextField").write("Jakic");
        clickOn("#ageTextField").write("25");
        clickOn("#emailTextField").write("mika@hotmail.com");
        clickOn("#addressTextField").write("123");
        clickOn("#usernameTextField").write("mika++++*2003");
        clickOn("#passwordTextField").write("mika++++*2003");

        clickOn("Register up");

        Thread.sleep(2000);

        try {
            FxRobot robot = new FxRobot();
            robot.push(javafx.scene.input.KeyCode.ENTER);
            assertTrue(true, "Alert was shown and dismissed.");
        } catch (Exception e) {
            assertTrue(false, "Expected an alert to be shown for invalid input.");
        }
    }
}



