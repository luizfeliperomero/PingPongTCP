package org.example;

import lombok.SneakyThrows;
import org.example.model.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable{
    Socket socket;
    ObjectOutputStream outputStream;

    public Client(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @Override
    public void run() {
        Packet packet = new Packet();
        packet.setType("Ping");
        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            Packet responsePacket = (Packet) objectInputStream.readObject();
            if(responsePacket.getType().equals("Pong")) {
                System.out.println(responsePacket.getType());
                System.out.println("Response Time: " + ((System.nanoTime() - responsePacket.getTimeNano())/1000000.00) + "ns");
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
