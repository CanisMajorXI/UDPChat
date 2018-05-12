package client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UDPClient {
    public static final DataSocketTheadLocal dataSocketThreadLocal = new DataSocketTheadLocal();
    public static volatile boolean isStop = false;
    public static InetAddress destAddress = null;
    public static int serial = new Random().nextInt(90000000) + 10000000;
    public static List<String> historyList = new ArrayList<>();
    public static List<String> latestList = new ArrayList<>();
    private DatagramSocket socket = null;
    private int port;

    UDPClient(InetAddress destAddress, int port) {
        this.destAddress = destAddress;
        this.port = port;
    }

    public void start() {
        new ChatRecvThread().start();
        new HeartBeatThread().start();
        Scanner scanner = new Scanner(System.in);
        while (!isStop) {
            String str = scanner.next();
            switch (str) {
                case "list":
                    new RequestForUserListThread().start();
                    break;
                case "recv":
                    for (String integratedMessage : latestList) {
                        String id = integratedMessage.substring(0, 8);
                        String message = integratedMessage.substring(8, integratedMessage.length());
                        System.out.println("ID:" + id + "信息：" + message);
                    }
                    latestList.clear();
                    break;
                case "send":
                    System.out.println("请输入接收者的id");
                    int id = scanner.nextInt();
                    System.out.println("请输入要发送的消息");
                    String message = scanner.next();
                    ChatSendThread.RunWithANewThread(id, message);
                    break;
                case "myid":
                    System.out.println("我的id：" + serial);
                    break;
            }
        }

    }

    static class DataSocketTheadLocal extends ThreadLocal<DatagramSocket> {
        @Override
        public DatagramSocket initialValue() {
            DatagramSocket datagramSocket = null;
            System.out.println("申请线程成功" + Thread.currentThread().getName());
            try {
                if (Thread.currentThread().getName().equals("recv"))
                    datagramSocket = new DatagramSocket(10010);
                else datagramSocket = new DatagramSocket();
            } catch (SocketException e) {
                System.out.println("申请socket失败，程序退出!");
                UDPClient.isStop = true;
            } finally {
                return datagramSocket;
            }
        }

        DatagramSocket getTimeOutSocket(int timeOut) {
            DatagramSocket datagramSocket = null;
            try {
                datagramSocket = get();
                datagramSocket.setSoTimeout(timeOut);
            } catch (SocketException e) {
                e.printStackTrace();
                UDPClient.isStop = true;
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



