package org.example;

import java.io.IOException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Servidor iniciando na porta " + PORT + "...");

        // A lista compartilhada de clientes conectados — criada UMA vez aqui,
        // e a MESMA referência é passada pra todo ClientHandler que criarmos.
        // CopyOnWriteArrayList já é thread-safe, lembra da explicação de antes.
        List<ClientHandler> clients = new CopyOnWriteArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Aguardando conexões de clientes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado! Endereço: " + clientSocket.getInetAddress());

                // Agora passamos a lista compartilhada junto
                ClientHandler handler = new ClientHandler(clientSocket, clients);
                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}