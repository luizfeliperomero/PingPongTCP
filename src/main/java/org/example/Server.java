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
        System.out.println("-----------------------------------");
        System.out.println("\u001B[32m" + "Server Started : " + "\u001B[37m" + dtf.format(LocalDateTime.now()) + "\u001B[0m");
        System.out.println("-----------------------------------");
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
