package com.example.cs230;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/file_storage_db";
    private static final String USER = "root";
    private static final String PASSWORD = "veljko1503";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public int updateFileInfo(String username, String fileName, String accessControl, String serverId) {
        String query = "INSERT INTO Files (username, file_name, access_control, server_id) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE access_control = VALUES(access_control), server_id = VALUES(server_id)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, fileName);
            pstmt.setString(3, accessControl);
            pstmt.setString(4, serverId);

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void grantAccess(int fileId, int userId, String accessLevel) {
        String query = "INSERT INTO FileAccess (file_id, user_id, access_level) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, fileId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, accessLevel);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkAccessRights(String username, String fileName, String accessLevel) {
        String query = "SELECT fa.access_level FROM FileAccess fa "
                + "JOIN Files f ON fa.file_id = f.file_id "
                + "JOIN Users u ON fa.user_id = u.user_id "
                + "WHERE u.username = ? AND f.file_name = ? AND fa.access_level = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, fileName);
            pstmt.setString(3, accessLevel);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean fileExists(String fileName) {
        String query = "SELECT 1 FROM Files WHERE file_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fileName);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getServerIdForFile(String fileName) {
        String query = "SELECT server_id FROM Files WHERE file_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fileName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("server_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> listFiles(String username) {
        String query = "SELECT file_name FROM Files WHERE username = ?";

        List<String> files = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                files.add(rs.getString("file_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return files;
    }

    public void removeFileInfo(int fileId) {
        removeFileAccess(fileId);

        String query = "DELETE FROM Files WHERE file_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, fileId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeFileAccess(int fileId) {
        String query = "DELETE FROM FileAccess WHERE file_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, fileId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getFileId(String fileName) {
        String query = "SELECT file_id FROM Files WHERE file_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fileName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("file_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getUserId(String username) {
        String query = "SELECT user_id FROM Users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Integer> getAllUserIdsExcept(String username) {
        String query = "SELECT user_id FROM Users WHERE username != ?";
        List<Integer> userIds = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIds;
    }
}
