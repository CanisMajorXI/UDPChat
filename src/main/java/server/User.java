package server;

import java.net.SocketAddress;

public class User {
    private int serial;
    private SocketAddress socketAddress;

    public User(int serial, SocketAddress socketAddress) {
        this.serial = serial;
        this.socketAddress = socketAddress;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
      //  return ((User) obj).getSerial() == this.getSerial() && ((User) obj).getSocketAddress() == this.getSocketAddress();
        return ((User) obj).getSerial() == this.getSerial();

    }
}
