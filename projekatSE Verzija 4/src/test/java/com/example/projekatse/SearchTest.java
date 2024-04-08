package com.example.projekatse;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage); // Start the main application
    }

    @Test
    public void testSearchFunctionality() {
        // Navigate to the login screen and perform a successful login to reach the LoggedInScene
        clickOn("Log-in");
        clickOn("#usernameTextField").write("veljko1503");
        clickOn("#passwordTextField").write("veljko1503");
        clickOn("#loginButton");

        sleep(1000); // Wait for the scene transition, adjust time as needed

        // Now in LoggedInScene, perform a search operation
        clickOn("#searchBar").write("No Longer Human");
        type(javafx.scene.input.KeyCode.ENTER);

        sleep(1000); // Wait for the search result and scene transition, adjust time as needed

        // Validate that the BookDescriptionScene is showing the correct book
        // This assumes you have a text element (e.g., a label) with ID "bookTitle" in the BookDescriptionScene showing the book's title
        assertTrue(lookup("#bookTitle").queryText().getText().contains("No Longer Human"), "Book title does not match.");
    }
}

