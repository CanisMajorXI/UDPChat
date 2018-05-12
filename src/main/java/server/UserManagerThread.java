package server;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class UserManagerThread extends Thread {
    private static ReentrantLock lock = new ReentrantLock();
    private static List<User> curUserList = new ArrayList<>();
    private static List<User> userList = curUserList;

    public static List<User> getUserList() {
        try {
            lock.lock();
            return new ArrayList<>(userList);
        } finally {
            lock.unlock();
        }
    }

    public static void addUser(int serial, SocketAddress socketAddress) {
        lock.lock();

        User user = new User(serial, socketAddress);
        if (!userList.contains(user)) userList.add(user);
        if (!curUserList.contains(user))
            curUserList.add(user);
        lock.unlock();
    }

    private static void updateUserList() {
        lock.lock();
        userList = curUserList;
        curUserList = new LinkedList<>();
        lock.unlock();
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(10000);
                updateUserList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
