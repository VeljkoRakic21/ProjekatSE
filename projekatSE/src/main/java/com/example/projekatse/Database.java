package com.example.projekatse;

import java.sql.*;

import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;



public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    /**
     * This functions serves for taking all information from inputs and inserting it into right database tables and rows
     * so that user is registered.
     * @param name
     * @param lastName
     * @param age
     * @param email
     * @param address
     * @param username
     * @param password
     */
    public static void registerUser(String name, String lastName, int age, String email, String address, String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root",
                "veljko1503")) {
            connection.setAutoCommit(false);
            logger.info("Connected to MySQL server");

            if (isUsernameExists(connection, username)) {
                showErrorDialog("Error: One other account is under this username, try another.");
                return;
            }

            String query = "INSERT INTO USER (NAME, LAST_NAME, AGE, EMAIL, ADDRESS, USERNAME, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setInt(3, age);
                statement.setString(4, email);
                statement.setString(5, address);
                statement.setString(6, username);
                statement.setString(7, password);
                statement.executeUpdate();

                connection.commit();

                System.out.println("User registered successfully.");
            }
        } catch (SQLException e) {
            logger.error("Failed to connect MySQL server: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This function serves to check if username already exist in database.
     * @param connection
     * @param username
     * @return
     * @throws SQLException
     */
    private static boolean isUsernameExists(Connection connection, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM USER WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    /**
     * This function serve to display error alert if user inputs something wrong.
     * @param message
     */
    private static void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(InputValidator.class.getResource("stylealert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("myAlert");
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: white;");
        alert.showAndWait();
    }

    /**
     * This function serves to authenticate user while he is logging in.
     * @param username
     * @param password
     * @return
     */
    public static boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root",
                "veljko1503")) {
            String query = "SELECT * FROM USER WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This function gets id for user that is currently logged in.
     * @param username
     * @return
     */
    public static int getUserId(String username) {
        int userId = -1;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT ID_USER FROM USER WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt("ID_USER");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve user ID: {}", e.getMessage());
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * This function gets book information from database and stores it in data class BookDetails.
     * @param bookId
     * @return
     */
    public static BookDetails getBookDetails(int bookId) {
        BookDetails bookDetails = new BookDetails();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "SELECT ID_BOOK, NAME, AUTHOR, SYNOPSIS, GENRE, PICTURE, AVAILABILITY, PRICE FROM BOOK WHERE ID_BOOK = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, bookId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        bookDetails.setId(resultSet.getInt("ID_BOOK"));
                        bookDetails.setName(resultSet.getString("NAME"));
                        bookDetails.setAuthor(resultSet.getString("AUTHOR"));
                        bookDetails.setSynopsis(resultSet.getString("SYNOPSIS"));
                        bookDetails.setGenre(resultSet.getString("GENRE"));
                        bookDetails.setAvailability(resultSet.getBoolean("AVAILABILITY"));
                        bookDetails.setPrice(resultSet.getInt("PRICE"));
                        Blob pictureBlob = resultSet.getBlob("PICTURE");
                        if (pictureBlob != null) {
                            bookDetails.setPicture(pictureBlob);
                        } else {
                            logger.warn("Picture Blob is null");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve book details: {}", e.getMessage());
            e.printStackTrace();
        }
        return bookDetails;
    }

    /**
     * This function serves to insert Rent details into right database tables and rows from user renting a book.
     * @param userId
     * @param bookId
     * @param cardNumber
     * @param cvv
     * @param dueDate
     */
    public static void insertRentDetails(int userId, int bookId, String cardNumber, int cvv, Date dueDate) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "veljko1503")) {
            String query = "INSERT INTO RENT (ID_USER, ID_BOOK, CARD_NUMBER, CVV, DATE, DUE_DATE) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, userId);
                statement.setInt(2, bookId);
                statement.setString(3, cardNumber);
                statement.setInt(4, cvv);
                statement.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
                statement.setDate(6, dueDate);
                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int rentId = resultSet.getInt(1);
                        BookDetails bookDetails = getBookDetails(bookId);
                        showRentSuccessAlert(bookDetails.getName(), bookDetails.getAuthor(), LocalDate.now());
                    }
                }
                logger.info("Rent details inserted successfully");
            }
        } catch (SQLException e) {
            logger.error("Failed to insert rent details: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This is alert that is shown when user does something successfully.
     * @param bookName
     * @param bookAuthor
     * @param rentDate
     */
    private static void showRentSuccessAlert(String bookName, String bookAuthor, LocalDate rentDate) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Rent Success");
        alert.setHeaderText(null);
        alert.setContentText(String.format("You have successfully rented %s by %s on %s.", bookName, bookAuthor, rentDate));
        Pane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Database.class.getResource("stylealert.css").toExternalForm());

        alert.showAndWait();
    }
}



