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
        new Main().start(stage);
    }

    @BeforeEach
    public void setUp() {
        openRegisterWindow.registrationSuccessAlertShown = false;
    }

    @AfterEach
    public void tearDown() {
        openRegisterWindow.registrationSuccessAlertShown = false;
    }

    @Test
    public void testSuccessfulRegistration() {
        clickOn("Register");

        clickOn("#nameTextField").write("Mika");
        clickOn("#lastNameTextField").write("Jakic");
        clickOn("#ageTextField").write("25");
        clickOn("#emailTextField").write("mika@hotmail.com");
        clickOn("#addressTextField").write("123");
        clickOn("#usernameTextField").write("mika++++*2003");
        clickOn("#passwordTextField").write("mika++++*2003");

        clickOn("Register up");
        sleep(2000);
        press(KeyCode.ENTER);

        // Verify the success alert was shown
        assertTrue(openRegisterWindow.registrationSuccessAlertShown, "Registration success alert was not shown.");
    }
}

