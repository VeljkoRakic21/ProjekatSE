package com.example.projekatse;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage);
    }

    @Test
    public void testSearchFunctionality() {
        clickOn("Log-in");
        clickOn("#usernameTextField").write("veljko1503");
        clickOn("#passwordTextField").write("veljko1503");
        clickOn("#loginButton");

        sleep(1000);

        clickOn("#searchBar").write("No Longer Human");
        type(javafx.scene.input.KeyCode.ENTER);

        sleep(1000);

        assertTrue(lookup("#bookTitle").queryText().getText().contains("No Longer Human"), "Book title does not match.");
    }
}

