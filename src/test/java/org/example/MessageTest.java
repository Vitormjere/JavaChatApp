package org.example;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    private final Gson gson = new Gson();

    @Test
    public void deveConverterMensagemParaJsonERecuperarValores() {
        // Arrange: cria uma mensagem de exemplo
        Message original = new Message("CHAT", "Vitor", "oi pessoal", "2026-07-16T10:00:00");

        // Act: converte pra JSON e depois de volta pra objeto
        String json = gson.toJson(original);
        Message recuperada = gson.fromJson(json, Message.class);

        // Assert: os valores devem ser os mesmos depois da "ida e volta"
        assertEquals("CHAT", recuperada.getType());
        assertEquals("Vitor", recuperada.getNick());
        assertEquals("oi pessoal", recuperada.getContent());
        assertEquals("2026-07-16T10:00:00", recuperada.getTimestamp());
    }
}