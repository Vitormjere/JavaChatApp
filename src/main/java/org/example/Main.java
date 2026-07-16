package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Servidor iniciando na porta " + PORT + "...");

        List<ClientHandler> clients = new CopyOnWriteArrayList<>();
        Database database = new Database();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Aguardando conexões de clientes...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado! Endereço: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, clients, database);
                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}