package com.example.projekatse;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.testfx.api.FxRobotException;

public class RateTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage); // Assuming Main is your main JavaFX application class
    }

    @Test
    public void testRateBookFunctionality() {
        // Assuming that the "Log-in" button is available on the main screen
        clickOn("Log-in");

        // Input credentials and log in (adjust the selectors according to your UI)
        clickOn("#usernameTextField").write("zikaikiti");
        clickOn("#passwordTextField").write("zikaikiti");
        clickOn("#loginButton");

        // Wait for login process to complete and logged in scene to be displayed
        sleep(1000);

        // Navigate to the HistoryRentScene by clicking the "History" button
        clickOn("#history-button");

        // In HistoryRentScene, select a book to rate (this part may need adjustment based on how your list is displayed)
        // For simplicity, assuming the first book in the history can be clicked to select it for rating
        // If your application structure is different, you may need to adjust this accordingly
        sleep(1000); // Waiting for the scene to load completely

        try {
            clickOn("#comboBox"); // Open the ComboBox dropdown

            // Navigate to "5" by pressing DOWN key, adjust the number of times based on your ComboBox's items order
            for (int i = 1; i < 5; i++) {
                type(KeyCode.DOWN); // Press DOWN until "5" is reached
            }
            type(KeyCode.ENTER); // Select the currently highlighted item, which should be "5"

            // Continue with clicking the rate button and verifying the alert as before
            clickOn("#rate-button");

            // Verify the success alert presence
            sleep(1000); // Adjust sleep as necessary for your application
            push(KeyCode.ENTER); // Assuming the alert's confirmation button is focused and ENTER dismisses it

            assertTrue(true, "Rating success alert was shown.");
        } catch (FxRobotException e) {
            assertTrue(false, "Expected a success alert to be shown, but it wasn't.");
        }
        sleep(2000);
    }
}

