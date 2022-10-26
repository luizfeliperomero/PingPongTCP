package org.example;

import lombok.SneakyThrows;
import org.example.model.Packet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        System.out.println("Server Started");
        while(!serverSocket.isClosed()) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Connection accepted from " + socket.getInetAddress());
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
                    System.out.println(packet.getType());
                    packet.setType("Pong");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(packet);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
