package org.example;

import org.example.model.Packet;

import java.io.IOException;
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
                System.out.println("Client Received: " + "\u001B[35m" + responsePacket.getType() + "\u001B[0m");
                double responseTime = (System.nanoTime() - responsePacket.getTimeNano())/1000000.00;
                String style = "";
                if(responseTime > 30 && responseTime < 40) {
                    style = "\u001B[33m";
                } else if (responseTime > 40){
                    style = "\u001B[31m";
                } else  {
                    style = "\u001b[32;1m";
                }
                System.out.println("Response Time: " + style + ((System.nanoTime() - responsePacket.getTimeNano())/1000000.00) + "\u001B[0m" + "ns");
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
