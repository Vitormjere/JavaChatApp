package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ChatClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Digite seu nickname: ");
        String nick = teclado.readLine();

        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);

        Message joinMessage = new Message("JOIN", nick, null, LocalDateTime.now().toString());
        writer.println(gson.toJson(joinMessage));

        System.out.println("Conectado! Digite mensagens e aperte Enter (Ctrl+C para sair):");

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

        String linhaDigitada;
        while ((linhaDigitada = teclado.readLine()) != null) {
            Message chatMessage = new Message("CHAT", nick, linhaDigitada, LocalDateTime.now().toString());
            writer.println(gson.toJson(chatMessage));
        }
    }
}