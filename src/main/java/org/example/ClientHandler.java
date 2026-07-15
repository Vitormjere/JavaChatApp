package org.example;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final List<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        // TODO 1: adiciona este handler na lista compartilhada
        clients.add(this);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {

            String mensagem;
            // TODO 2: loop que lê mensagens até a conexão fechar (readLine() retorna null)
            while ((mensagem = reader.readLine()) != null) {
                System.out.println("Mensagem recebida: " + mensagem);
            }

        } catch (IOException e) {
            System.err.println("Erro ao lidar com cliente: " + e.getMessage());
        } finally {
            // TODO 3: remove este handler da lista, aconteça o que acontecer
            clients.remove(this);

            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}