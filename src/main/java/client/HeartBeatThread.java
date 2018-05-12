package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class HeartBeatThread extends Thread {
    private DatagramSocket socket = null;
    private InetAddress destAddress = null;
    private int serial;

    HeartBeatThread() {
        this.destAddress = UDPClient.destAddress;
        serial = UDPClient.serial;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = UDPClient.dataSocketThreadLocal.getTimeOutSocket(3000);
            if (socket == null) {
                throw new Exception("socketfail");
            }
            byte[] sendBytes = getBytesByInt(serial);
            DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, destAddress, 10087);
            byte[] recvBytes = new byte[0];
            DatagramPacket recvPacket = new DatagramPacket(recvBytes, recvBytes.length);
            while (!UDPClient.isStop) {
                socket.send(sendPacket);
                socket.receive(recvPacket);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            if (e.getMessage().equals("socketfail")) {
                System.out.println("获取本地Socket失败，程序退出");
            } else {
                System.out.println("与服务器连接中断！程序退出");
                UDPClient.isStop = true;
            }

        } finally {
            UDPClient.dataSocketThreadLocal.closeSocket();
        }
    }

    private byte[] getBytesByInt(int serial) {
        return (serial + "").getBytes();
    }
}
