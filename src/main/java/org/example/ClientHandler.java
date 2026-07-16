package org.example;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final List<ClientHandler> clients;
    private PrintWriter writer;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        clients.add(this);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {

            writer = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()), true);

            String mensagem;
            while ((mensagem = reader.readLine()) != null) {
                System.out.println("Mensagem recebida: " + mensagem);
                broadcast(mensagem);
            }

        } catch (IOException e) {
            System.err.println("Erro ao lidar com cliente: " + e.getMessage());
        } finally {
            clients.remove(this);
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    private void broadcast(String mensagem) {
        for (ClientHandler client : clients) {
            if (client != this) {
                client.sendMessage(mensagem);
            }
        }
    }

    public void sendMessage(String mensagem) {
        if (writer != null) {
            writer.println(mensagem);
        }
    }
}