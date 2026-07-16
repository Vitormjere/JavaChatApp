package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final List<ClientHandler> clients;
    private final Database database;
    private PrintWriter writer;
    private String nick;
    private static final Gson gson = new Gson();

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients, Database database) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.database = database;
    }

    @Override
    public void run() {
        clients.add(this);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()))) {

            writer = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()), true);

            String linha;
            while ((linha = reader.readLine()) != null) {
                Message message = gson.fromJson(linha, Message.class);

                if ("JOIN".equals(message.getType())) {
                    this.nick = message.getNick();
                    broadcast(nick + " entrou no chat.");
                } else if ("CHAT".equals(message.getType())) {
                    String formatada = message.getNick() + ": " + message.getContent();
                    System.out.println(formatada);
                    broadcast(formatada);
                    database.saveMessage(message.getNick(), message.getContent(), message.getTimestamp());
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao lidar com cliente: " + e.getMessage());
        } finally {
            clients.remove(this);
            if (nick != null) {
                broadcast(nick + " saiu do chat.");
            }
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