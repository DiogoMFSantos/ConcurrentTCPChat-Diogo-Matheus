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
            username = input.readLine();

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

                    if (newName.isEmpty()){
                        clientManager.broadcast("You have to write a name");
                        continue;
                    }

                    username = newName;
                    clientManager.broadcast(
                            "You are now known as "+ newName
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

            clientManager.broadcast(
                    username + " left the chat."
            );
        }
    }

    public void send(String message) {

        output.println(message);
    }

    public String getUsername() {

        return username;
    }
}