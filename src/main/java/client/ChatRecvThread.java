package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ChatRecvThread extends Thread {
    @Override
    public void run() {
        setName("recv");
        try {
            DatagramSocket socket = UDPClient.dataSocketThreadLocal.get();
            if (socket == null) {
                throw new Exception();
            }

            while (!UDPClient.isStop) {
                byte[] recBytes = new byte[400];
                DatagramPacket recvPacket = new DatagramPacket(recBytes, recBytes.length);
                socket.receive(recvPacket);
                String message = new String(recBytes);
                int index = message.indexOf(0);
                message = message.substring(0, index);
                UDPClient.latestList.add(message);
                UDPClient.historyList.add(message);
            }
        } catch (Exception e) {
            System.out.println("连接出现问题！");
            UDPClient.isStop = true;
        } finally {
            UDPClient.dataSocketThreadLocal.closeSocket();
        }
    }
}
