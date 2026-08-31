package org.example;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {

    private final List<ClientHandler> clients =
            new ArrayList<>();

    public synchronized void addClient(
            ClientHandler client) {

        clients.add(client);
    }

    public synchronized void removeClient(
            ClientHandler client) {

        clients.remove(client);
    }

    public synchronized void broadcast(
            String message) {

        for (ClientHandler client : clients) {

            client.send(message);
        }
    }
    public synchronized void sendList(
            ClientHandler requester) {

        requester.send("Connected users:");

        for (ClientHandler client : clients) {
            requester.send(client.getUsername());
        }
    }

    public synchronized void whisper(
            ClientHandler requester,
            String targetUsername,
            String message) {

        for (ClientHandler client : clients) {

            if (client.getUsername() != null && client.getUsername().equalsIgnoreCase(targetUsername)) {

                client.send(
                        "[Whisper from "
                                + requester.getUsername()
                                + "]: "
                                + message
                );

                requester.send(
                        "[Whisper to "
                                + client.getUsername()
                                + "]: "
                                + message
                );

                return;
            }
        }

        requester.send(
                "User not found: " + targetUsername
        );
    }

    public synchronized boolean usernameExists(String username) {

        for (ClientHandler client : clients) {

            if (client.getUsername() != null &&
                    client.getUsername().equalsIgnoreCase(username)) {

                return true;
            }
        }

        return false;
    }

}