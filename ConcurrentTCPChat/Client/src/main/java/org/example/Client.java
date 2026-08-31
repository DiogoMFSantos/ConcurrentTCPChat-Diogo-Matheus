package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String HOST = "localhost";
    private static final int DEFAULT_PORT_CONTROL = 5001;

    public static void main(String[] args) {

        try (Socket socket =
                     new Socket(HOST, DEFAULT_PORT_CONTROL)) {

            System.out.println(
                    "Connected to chat server."
            );

            BufferedReader serverInput =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()
                            )
                    );

            PrintWriter serverOutput =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true
                    );

            Scanner scanner =
                    new Scanner(System.in);

            // Set username
            String userName;

            do {
                System.out.print("Enter username: ");
                userName = scanner.nextLine().trim();

                if (userName.isEmpty()) {
                    System.out.println("Username cannot be empty.");
                }

            } while (userName.isEmpty());

            serverOutput.println(userName);

            // Thread for receiving messages
            Thread receiver = new Thread(() -> {

                try {

                    String message;

                    while ((message =
                            serverInput.readLine()) != null) {

                        System.out.println(message);
                    }

                } catch (IOException e) {

                    System.out.println(
                            "Disconnected from server."
                    );
                }
            });

            receiver.start();

            // Main thread sends messages
            while (true) {

                String message = scanner.nextLine();

                serverOutput.println(message);

                if (message.equals("/quit")) {
                    break;
                }

            }

        } catch (IOException e) {

            System.out.println(
                    "Could not connect to server: "
                            + e.getMessage()
            );
        }

    }
}