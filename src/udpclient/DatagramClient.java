package udpclient;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class DatagramClient{

    private MulticastSocket multicastSocket;
    private InetAddress address;
    private Scanner scanner;
    private String msg;

    public DatagramClient() throws IOException {
        multicastSocket = new MulticastSocket(12345);
        address = InetAddress.getByName("225.4.5.6");
        multicastSocket.joinGroup(address);
        scanner = new Scanner(System.in);
        Thread incomingMessagesThread = new Thread(new incomingMessages());
        incomingMessagesThread.start();
    }

    void outgoingMessages() throws IOException {
        while (true) {
            msg = scanner.nextLine();
            //Write
            DatagramPacket datagramPacketOut = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, 5000);
            multicastSocket.send(datagramPacketOut);
        }
    }

    public static void main(String[] args) throws IOException {

        new DatagramClient().outgoingMessages();

    }

    public class incomingMessages implements Runnable{
        @Override
        public void run() {
            //read
            while(true) {
                DatagramPacket datagramPacketIn = new DatagramPacket(new byte[1024], 1024);
                try {
                    multicastSocket.receive(datagramPacketIn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = new String(datagramPacketIn.getData(), 0, datagramPacketIn.getLength()).trim();
                System.out.println(data);
            }

        }
    }
}
