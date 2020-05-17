package ro.pub.cs.systems.eim.practice_eim2;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

    public String address;
    int port;
    String city;
    String type;
    TextView view;

    public Client(String address, int port, String city, String type, TextView view) {
        this.address = address;
        this.port = port;
        this.city = city;
        this.type = type;
        this.view = view;
    }

    Socket socket;

    @Override
    public void run() {

        try {
            socket = new Socket(address, port);
            BufferedReader reader = Utils.getReader(socket);
            PrintWriter writer = Utils.getWriter(socket);

            writer.println(city);
            writer.flush();
            writer.println(type);
            writer.flush();

            String info;

            while ((info = reader.readLine()) != null) {
                final String finalInfo = info;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setText(finalInfo);
                    }
                });
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
