package org.example;

import org.example.model.Packet;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(8081);
        Client client = new Client("localhost", 8081);

        Thread clientThread = new Thread(server);
        Thread serverThread = new Thread(client);

        clientThread.start();
        serverThread.start();
    }
}