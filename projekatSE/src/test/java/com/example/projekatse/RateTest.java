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
        new Main().start(stage);
    }

    @Test
    public void testRateBookFunctionality() {
        clickOn("Log-in");

        clickOn("#usernameTextField").write("zikaikiti");
        clickOn("#passwordTextField").write("zikaikiti");
        clickOn("#loginButton");

        sleep(1000);

        clickOn("#history-button");

        sleep(1000);

        try {
            clickOn("#comboBox");
            for (int i = 1; i < 5; i++) {
                type(KeyCode.DOWN);
            }
            type(KeyCode.ENTER);

            clickOn("#rate-button");

            sleep(1000);
            push(KeyCode.ENTER);

            assertTrue(true, "Rating success alert was shown.");
        } catch (FxRobotException e) {
            assertTrue(false, "Expected a success alert to be shown, but it wasn't.");
        }
        sleep(2000);
    }
}

