package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RequestForUserListThread extends Thread {
    private DatagramSocket socket = null;
    private InetAddress destAddress = null;

    public RequestForUserListThread() {
        this.destAddress = UDPClient.destAddress;
    }

    @Override
    public void run() {
        DatagramSocket socket = UDPClient.dataSocketThreadLocal.get();
        if (socket == null) return;
        byte[] bytes = new byte[0];
        byte[] recvBytes = new byte[240];
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, destAddress, 10088);
        DatagramPacket recvPacket = new DatagramPacket(recvBytes, recvBytes.length);
        try {
            socket.send(sendPacket);
            socket.receive(recvPacket);
            System.out.println("当前用户列表:");
            int index = 240;
                    for (int i = 0; i < 240; i+=8) {
                    if (recvBytes[i] == 0) {
                    break;
                }
                for(int j = 0;j<8;j++){
                        System.out.print((char)recvBytes[i+j]);
                        //stringBuffer.append(recvBytes[i+j]);
                }
              System.out.print(" ");
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取用户列表失败");
        } finally {
            UDPClient.dataSocketThreadLocal.closeSocket();
        }
    }

    private DatagramSocket getDatagramSocket() {
        try {
            if (socket == null) {
                socket = new DatagramSocket();
                socket.setSoTimeout(2000);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("服务器10088端口被占用!");
            UDPClient.isStop = true;
        } finally {
            return socket;
        }
    }
}
