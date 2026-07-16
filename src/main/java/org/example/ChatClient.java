package org.example;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) throws IOException {
        System.out.println("Conectando ao servidor...");

        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        System.out.println("Conectado! Digite mensagens e aperte Enter (Ctrl+C para sair):");

        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);

        // Thread separada para escutar mensagens vindas do servidor,
        // enquanto a thread principal cuida do teclado.
        Thread listenerThread = new Thread(() -> {
            try {
                BufferedReader serverReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String linhaRecebida;
                while ((linhaRecebida = serverReader.readLine()) != null) {
                    System.out.println(linhaRecebida);
                }
            } catch (IOException e) {
                System.err.println("Conexão com o servidor encerrada.");
            }
        });
        listenerThread.start();

        BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in));

        String linhaDigitada;
        while ((linhaDigitada = teclado.readLine()) != null) {
            writer.println(linhaDigitada);
        }
    }
}