package client;

import java.net.InetAddress;

public class Main {
    public static void main(String[] args) throws Exception{
        UDPClient client = new UDPClient(InetAddress.getLocalHost(),10086);
        //client.test();
        client.start();
    }
}
