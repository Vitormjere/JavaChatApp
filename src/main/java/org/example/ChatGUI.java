package org.example;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ChatGUI extends Application {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final Gson gson = new Gson();

    private Socket socket;
    private PrintWriter writer;
    private String nick;

    private TextArea messageArea;
    private TextField inputField;

    @Override
    public void start(Stage stage) {
        // Pede o nickname antes de abrir a tela principal
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Digite seu nickname:");
        dialog.setTitle("Java Chat App");
        var result = dialog.showAndWait();

        if (result.isEmpty() || result.get().isBlank()) {
            Platform.exit();
            return;
        }
        nick = result.get();

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Digite sua mensagem e aperte Enter...");
        inputField.setOnAction(e -> sendMessage());

        Button sendButton = new Button("Enviar");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));
        HBox.setHgrow(inputField, javafx.scene.layout.Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setCenter(messageArea);
        root.setBottom(inputBox);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Java Chat App - " + nick);
        stage.setScene(scene);
        stage.show();

        // Fecha a conexão de rede quando a janela for fechada
        stage.setOnCloseRequest(e -> {
            try {
                if (socket != null) socket.close();
            } catch (IOException ignored) {}
        });

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true);

            Message joinMessage = new Message("JOIN", nick, null, LocalDateTime.now().toString());
            writer.println(gson.toJson(joinMessage));

            // Thread separada para escutar o servidor, igual fizemos no ChatClient de terminal
            Thread listenerThread = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        String mensagemFinal = linha;
                        // IMPORTANTE: atualizações de UI no JavaFX só podem acontecer
                        // na thread própria da interface (JavaFX Application Thread).
                        // Platform.runLater agenda essa atualização pra rodar nela,
                        // em vez de tentar mexer na tela direto desta thread de rede.
                        Platform.runLater(() -> messageArea.appendText(mensagemFinal + "\n"));
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> messageArea.appendText("[Conexão encerrada]\n"));
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

        } catch (IOException e) {
            messageArea.appendText("Erro ao conectar: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String texto = inputField.getText();
        if (texto == null || texto.isBlank()) return;

        Message chatMessage = new Message("CHAT", nick, texto, LocalDateTime.now().toString());
        writer.println(gson.toJson(chatMessage));

        messageArea.appendText("Você: " + texto + "\n");

        inputField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}