package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class GetUserListThread extends Thread {
    private DatagramSocket socket = null;

    @Override
    public void run() {
        Thread.currentThread().setName("socket10088");
        byte[] recvBytes = new byte[0];
        DatagramPacket recvPacket = new DatagramPacket(recvBytes, recvBytes.length);
        try {
            while (!UDPServer.isStop) {
                DatagramSocket socket = UDPServer.dataSocketThreadLocal.get();
                if (socket == null) throw new Exception("socketerror");
                socket.receive(recvPacket);
                byte[] sendBytes = getBytesByUserList(UserManagerThread.getUserList());
                DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, recvPacket.getSocketAddress());
                socket.send(sendPacket);
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

    private byte[] getBytesByUserList(List<User> list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (User user : list) {
            stringBuffer.append(user.getSerial());
        }
        String str = stringBuffer.toString();
        if (str.length() > 240)
            str = str.substring(0, 240);
        byte[] bytes = str.getBytes();
        return bytes;
    }
}
