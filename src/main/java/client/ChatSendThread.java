package client;

import sun.plugin2.message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatSendThread implements Callable<Boolean> {
    private int id;
    private String message = null;
    private static DatagramSocket socket = null;
    private static ExecutorService service = null;

    private ChatSendThread(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public static void initialize() throws Exception {
        socket = UDPClient.dataSocketThreadLocal.get();
        service = Executors.newCachedThreadPool();
        if (socket == null) throw new Exception("socketerror");
    }

    public static void RunWithANewThread(int id, String message) {
        try {
            if (socket == null) initialize();
            service.submit(new ChatSendThread(id, message));
        } catch (Exception e) {
            UDPClient.isStop = true;
            if (e.getMessage().equals("socketerror")) {
                System.out.println("socket获取失败，程序退出!");
            } else {
                System.out.println("网络出现问题，程序退出！");
            }
        }
    }

    @Override
    public Boolean call() throws Exception {
        String integratedMessage = id + message;
        if (integratedMessage.length() > 400) integratedMessage = integratedMessage.substring(0, 400);
        byte[] sendBytes = integratedMessage.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, UDPClient.destAddress, 10089);
        DatagramSocket socket = new DatagramSocket();
       socket.send(sendPacket);
        return true;
    }

}
