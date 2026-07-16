package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    private static final String TEST_DB_FILE = "test_chat_history.db";

    @Test
    public void deveSalvarMensagemEConseguirRecupera() throws SQLException {
        // Arrange
        Database database = new Database(TEST_DB_FILE);

        // Act
        database.saveMessage("Vitor", "mensagem de teste", "2026-07-16T10:00:00");

        // Assert: consulta direto no banco pra confirmar que foi salvo
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + TEST_DB_FILE);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM messages WHERE nick = 'Vitor'")) {

            assertEquals(true, rs.next()); // deve existir pelo menos uma linha
            assertEquals("mensagem de teste", rs.getString("content"));
        }
    }

    @AfterEach
    public void limparBancoDeTeste() {
        // Remove o arquivo de banco de teste depois de cada teste,
        // pra não acumular lixo nem interferir em execuções futuras
        new File(TEST_DB_FILE).delete();
    }
}