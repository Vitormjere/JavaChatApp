package org.example;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Conectando ao servidor...");

        // Socket aqui já conecta direto, ao contrário do ServerSocket do servidor.
        // "localhost" significa "essa mesma máquina" — útil pra testar tudo no seu PC.
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {

            System.out.println("Conectado ao servidor!");

            // TODO 1: crie um PrintWriter a partir de socket.getOutputStream()
            PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()),
                    true
            );

            // TODO 2: use o PrintWriter pra mandar uma linha de texto
            writer.println("Olá, servidor! Esta é minha primeira mensagem.");

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}