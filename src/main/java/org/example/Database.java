package org.example;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:chat_history.db";

    public Database() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nick TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "timestamp TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public void saveMessage(String nick, String content, String timestamp) {
        String sql = "INSERT INTO messages (nick, content, timestamp) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nick);
            pstmt.setString(2, content);
            pstmt.setString(3, timestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar mensagem: " + e.getMessage());
        }
    }
}