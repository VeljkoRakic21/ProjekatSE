package com.example.projekatse;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTrueTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Start your main application
        new Main().start(stage);
    }

    @BeforeEach
    public void setUp() {
        // Reset the flag before each test
        openRegisterWindow.registrationSuccessAlertShown = false;
    }

    @AfterEach
    public void tearDown() {
        // Reset the flag after each test if needed
        openRegisterWindow.registrationSuccessAlertShown = false;
    }

    @Test
    public void testSuccessfulRegistration() {
        // Open the registration window
        clickOn("Register");

        // Fill in the registration form
        // Make sure to replace these with the correct identifiers for your input fields
        clickOn("#nameTextField").write("Mika");
        clickOn("#lastNameTextField").write("Jakic");
        clickOn("#ageTextField").write("25");
        clickOn("#emailTextField").write("mika@hotmail.com");
        clickOn("#addressTextField").write("123");
        clickOn("#usernameTextField").write("mika++++*2003");
        clickOn("#passwordTextField").write("mika++++*2003");

        // Click the "Register now" button
        // Adjust the text if your button has a different label
        clickOn("Register up");
        sleep(2000); // You may need to adjust this value based on the responsiveness of your application
        press(KeyCode.ENTER);

        // Verify the success alert was shown
        assertTrue(openRegisterWindow.registrationSuccessAlertShown, "Registration success alert was not shown.");
    }
}

