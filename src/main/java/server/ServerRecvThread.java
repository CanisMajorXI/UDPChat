package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ServerRecvThread extends Thread {
    @Override
    public void run() {
        Thread.currentThread().setName("socket10089");
        try {
            while (!UDPServer.isStop) {
                DatagramSocket socket = UDPServer.dataSocketThreadLocal.get();
                if (socket == null) {
                    throw new Exception("socketerror");
                }
                byte[] bytes = new byte[400];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                socket.receive(packet);
                String integratedMessage = new String(bytes);
                if (integratedMessage.length() < 8) throw new Exception();
                new ServerSendThread(integratedMessage).start();
            }
        } catch (Exception e) {
            if (e.getMessage().equals("socketerror")) {
                System.out.println("Socket错误！程序退出");
            } else {
                System.out.println("发生网络问题！程序退出");
                UDPServer.isStop = true;
            }
        }
    }
}

