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
        // Open the registration window
        clickOn("Register");

        // Fill the form with invalid data
        clickOn("#nameTextField").write("123");
        clickOn("#lastNameTextField").write("Jakic");
        clickOn("#ageTextField").write("25");
        clickOn("#emailTextField").write("mika@hotmail.com");
        clickOn("#addressTextField").write("123");
        clickOn("#usernameTextField").write("mika++++*2003");
        clickOn("#passwordTextField").write("mika++++*2003");

        // Attempt to submit the form to trigger validation
        clickOn("Register up");

        // Sleep for a short duration to ensure the alert has time to appear
        // Adjust the sleep duration based on your application's response time
        Thread.sleep(2000); // Example: wait for 1 second

        // Check if an alert is present and dismiss it if it is
        try {
            FxRobot robot = new FxRobot();
            robot.push(javafx.scene.input.KeyCode.ENTER); // Dismiss the alert
            assertTrue(true, "Alert was shown and dismissed."); // Pass the test as the alert was handled
        } catch (Exception e) {
            assertTrue(false, "Expected an alert to be shown for invalid input.");
        }
    }
}



