package org.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ClientManager clientManager;

    private BufferedReader input;
    private PrintWriter output;

    private String username;

    public ClientHandler(
            Socket socket,
            ClientManager clientManager) {

        this.socket = socket;
        this.clientManager = clientManager;
    }

    @Override
    public void run() {

        try {

            input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            // First message is the username
            String requestedUsername = input.readLine();

            if (requestedUsername == null) {
                return;
            }

            requestedUsername = requestedUsername.trim();

            if (clientManager.usernameExists(requestedUsername)) {
                send("Username already in use.");
                return;
            }

            username = requestedUsername;

            if (username == null) {
                return;
            }

            System.out.println(
                    username + " joined the chat."
            );

            clientManager.broadcast(
                    username + " joined the chat."
            );

            String message;

            while ((message = input.readLine()) != null) {

                if (message.equals("/quit")||message.equals("/bye")||message.equals("/disconnect")||message.equals("/exit")) {
                    break;
                }
                if (message.equals("/list")) {

                    clientManager.sendList(this);

                    continue;
                }
                if (message.equals("/help")) {

                    Help help = new Help(this);
                    help.run();

                    continue;
                }
                if (message.startsWith("/name ")) {
                    String newName = message.substring(6).trim();

                    if (newName.isEmpty()) {
                        send("You have to write a name");
                        continue;
                    }

                    if (clientManager.usernameExists(newName)) {
                        send("Username already in use.");
                        continue;
                    }

                    String oldName = username;
                    username = newName;

                    clientManager.broadcast(
                            oldName + " is now known as " + newName
                    );

                    continue;
                }

                if (message.startsWith("/whisper ")) {

                    String[] parts = message.split(" ", 3);

                    if (parts.length < 3) {
                        send("Usage: /whisper <username> <message>");
                        continue;
                    }

                    String targetUsername = parts[1];
                    String privateMessage = parts[2];

                    clientManager.whisper(
                            this,
                            targetUsername,
                            privateMessage
                    );

                    continue;
                }

                clientManager.broadcast(
                        username + ": " + message
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Connection error with " + username
            );

        } finally {

            clientManager.removeClient(this);

            try {
                socket.close();
            } catch (IOException e) {
                // Ignore
            }
            if (username != null) {
                clientManager.broadcast(
                        username + " left the chat."
                );
            }

        }
    }

    public void send(String message) {

        output.println(message);
    }

    public String getUsername() {

        return username;
    }
}