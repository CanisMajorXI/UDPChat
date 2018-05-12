package server;

import client.UDPClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {
    public static final DataSocketTheadLocal dataSocketThreadLocal = new DataSocketTheadLocal();
    private DatagramSocket socket = null;
    public volatile static boolean isStop = false;

    public void start() {
        new HeartBeatRecvThread().start();
        new GetUserListThread().start();
        new UserManagerThread().start();
        new ServerRecvThread().start();
    }

    public DatagramSocket getDatagramSocket() {
        try {
            if (socket == null) {
                socket = new DatagramSocket(10086);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("10086端口被占用!");
        } finally {
            return socket;
        }
    }

    static class DataSocketTheadLocal extends ThreadLocal<DatagramSocket> {
        @Override
        public DatagramSocket initialValue() {
            DatagramSocket datagramSocket = null;
            System.out.println("申请线程成功" + Thread.currentThread().getName());
            try {
                String threadName = Thread.currentThread().getName();
                threadName = threadName.substring(6, threadName.length());
                datagramSocket = new DatagramSocket(Integer.parseInt(threadName));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("申请socket失败，程序退出!");
                UDPServer.isStop = true;
            } finally {
                return datagramSocket;
            }
        }

        DatagramSocket getTimeOutSocket(int timeOut) {
            DatagramSocket datagramSocket = null;
            try {
                datagramSocket = get();
                datagramSocket.setSoTimeout(timeOut);
            } catch (Exception e) {
                e.printStackTrace();
                UDPServer.isStop = true;
                System.out.println("申请限时Socket失败，程序退出!");
            } finally {
                return datagramSocket;
            }
        }

        void closeSocket() {
            DatagramSocket socket = get();
            if (!socket.isClosed())
                socket.close();
        }
    }
}

