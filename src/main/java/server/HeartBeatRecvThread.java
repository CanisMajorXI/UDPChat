package server;

import client.UDPClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class HeartBeatRecvThread extends Thread {
    private DatagramSocket socket = null;

    @Override
    public void run() {
        Thread.currentThread().setName("socket10087");
        byte[] recvBytes = new byte[8];
        byte[] sendBytes = new byte[5];
        DatagramPacket recvPacket = new DatagramPacket(recvBytes, recvBytes.length);
        try {
            while (!UDPServer.isStop) {
                DatagramSocket socket = UDPServer.dataSocketThreadLocal.get();
                if(socket == null) {
                    throw new Exception("socketerror");
                }
                socket.receive(recvPacket);
                String str = new String(recvBytes);
               System.out.print("serial:" + str);
                String serialstr = str.substring(0,8);
                String portstr = str.substring(8,str.length());
                int serial = Integer.parseInt(serialstr);
                 UserManagerThread.addUser(serial,recvPacket.getSocketAddress());
                DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, recvPacket.getSocketAddress());
                socket.send(sendPacket);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
