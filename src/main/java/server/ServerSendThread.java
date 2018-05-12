package server;

import javafx.util.Pair;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class ServerSendThread extends Thread {
    private String message;

    public ServerSendThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            //DatagramSocket socket = UDPServer.dataSocketThreadLocal.get();
//            if (socket == null) {
//                throw new Exception("socketerror");
//            }
            SocketAddress socketAddress = null;
            int serial = Integer.parseInt(message.substring(0, 8));
            List<User> list = UserManagerThread.getUserList();
            for (User user : list) {
                if (user.getSerial() == serial) {
                    socketAddress = user.getSocketAddress();
                    break;
                }
            }
            if (socketAddress == null) return;
            if (message.length() > 400) message = message.substring(0, 400);
            byte[] bytes = message.getBytes();
            InetSocketAddress inetSocketAddress1 = (InetSocketAddress)socketAddress;
            InetSocketAddress inetSocketAddress2 = new InetSocketAddress(inetSocketAddress1.getAddress(),10010);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, inetSocketAddress2);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
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
