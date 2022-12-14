package org.example;

import lombok.SneakyThrows;
import org.example.model.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server implements Runnable{
    ServerSocket serverSocket;
    InputStream inputStream;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String serverStartedOutput = "Server Started on port " + serverSocket.getLocalPort() + " " + dtf.format(LocalDateTime.now());
        String serverStartedOutputFormatted = "\u001B[32m" + "Server Started on port " + serverSocket.getLocalPort() + " " + "\u001B[37m" + dtf.format(LocalDateTime.now()) + "\u001B[0m";
        String dash = "-";
        String title = "Ping-Pong";
        int topDashesHalfLength = (serverStartedOutput.length() - title.length())/2;
        System.out.println(dash.repeat(topDashesHalfLength) + title + dash.repeat(topDashesHalfLength));
        System.out.println(serverStartedOutputFormatted);
        System.out.println(dash.repeat(serverStartedOutput.length()));
        while(!serverSocket.isClosed()) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Connection accepted from " + "\u001B[36m" + socket.getInetAddress() + "\u001B[0m");
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ObjectInputStream objectReceived = null;
            try {
                objectReceived = new ObjectInputStream(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Packet packet = (Packet) objectReceived.readObject();
                if(packet.getType().equals("Ping")) {
                    System.out.println("Server Received: " + "\u001B[35m" + packet.getType() + "\u001B[0m");
                    packet.setType("Pong");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(packet);
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
