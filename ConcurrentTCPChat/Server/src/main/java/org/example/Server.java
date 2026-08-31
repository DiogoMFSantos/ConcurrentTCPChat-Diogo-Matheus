package org.example;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Server {
    public static final String ROOT_FOLDER = "serverRoot/";

    private static final int PORT = 5001;

    public static void main(String[] args) {

        ClientManager clientManager = new ClientManager();

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            System.out.println("Chat server started.");
            System.out.println(
                    "Listening on port " + PORT
            );

            while (true) {

                Socket clientSocket =
                        serverSocket.accept();

                System.out.println(
                        "Client connected: "
                                + clientSocket
                );

                ClientHandler clientHandler =
                        new ClientHandler(
                                clientSocket,
                                clientManager
                        );

                clientManager.addClient(clientHandler);

                Thread thread =
                        new Thread(clientHandler);

                thread.start();
            }

        } catch (IOException e) {

            System.out.println(
                    "Server error: "
                            + e.getMessage()
            );
        }
    }
}