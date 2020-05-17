package ro.pub.cs.systems.eim.practice_eim2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread {

    int port;
    ServerSocket socket;
    HashMap<String, Info> memory;

    public Server(int port) {
        this.port = port;

        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        memory = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Socket s = socket.accept();
                Comm comm = new Comm(this, s);
                comm.start();;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        interrupt();

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setData(String city, Info info) {
        this.memory.put(city, info);
    }

    public synchronized HashMap<String, Info> getData() {
        return this.memory;
    }

}
