package com.udpchatroom;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class DatagramServer implements Runnable{

    private MulticastSocket multicastSocket;
    private DatagramPacket datagramPacketIn;
    private String data;
    private InetAddress group;
    private DatagramPacket datagramPacketOut;
    private String username;
    private String message;
    private String[] parts;

    public DatagramServer() throws IOException {
        multicastSocket = new MulticastSocket(5000);
        group = InetAddress.getByName("225.4.5.6");
        multicastSocket.joinGroup(group);
        System.out.println("Server started on port 5000");
    }

    public static void main(String[] args) throws IOException {
        new Thread(new DatagramServer()).start();
    }

    @Override
    public void run() {
        datagramPacketIn = new DatagramPacket(new byte[1024],1024);
        while(true){
            try {
                //read
                multicastSocket.receive(datagramPacketIn);
                data = new String(datagramPacketIn.getData(),0,datagramPacketIn.getLength()).trim();
                parts = data.split("/");
                username = parts[0];
                message = parts[1];
                System.out.println(data);
                //write
                datagramPacketOut = new DatagramPacket(data.getBytes(), data.getBytes().length , group , datagramPacketIn.getPort());
                multicastSocket.send(datagramPacketOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}