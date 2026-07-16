package org.example;

import java.sql.*;

public class Database {
    private final String url;

    public Database() {
        this("chat_history.db");
    }

    public Database(String fileName) {
        this.url = "jdbc:sqlite:" + fileName;
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nick TEXT NOT NULL," +
                "content TEXT NOT NULL," +
                "timestamp TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public void saveMessage(String nick, String content, String timestamp) {
        String sql = "INSERT INTO messages (nick, content, timestamp) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
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