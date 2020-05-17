package ro.pub.cs.systems.eim.practice_eim2;

import android.os.Build;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import androidx.annotation.RequiresApi;

public class Comm extends Thread {

    Server server;
    Socket socket;

    public Comm(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try {
            BufferedReader reader = Utils.getReader(socket);
            PrintWriter writer = Utils.getWriter(socket);

            String city = reader.readLine();
            String type = reader.readLine();

            HashMap<String, Info> data = server.getData();

            Info info = new Info();

            if (data.containsKey(city)) {
                info = data.get(city);
            } else {
                HttpClient httpClient = new DefaultHttpClient();
                String page;

                HttpGet httpGet = new HttpGet("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=e03c3b32cfb5a6f7069f2ef29237d87e");
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                page = EntityUtils.toString(entity);


                JSONObject content = new JSONObject(page);
                JSONArray weather = content.getJSONArray("weather");

                info.condition = weather.getJSONObject(0).getString("main");


                JSONObject main =content.getJSONObject("main");

                info.temperature = main.getString("temp");
                info.pressure = main.getString("pressure");
                info.humidity = main.getString("humidity");

                JSONObject wind = content.getJSONObject("wind");
                info.windSpeed = wind.getString("speed");

                server.setData(city, info);
            }

            String result;

            switch (type) {
                case "all":
                    result = info.toString();
                    break;
                case "temperature":
                    result = info.temperature;
                    break;
                case "wind_speed":
                    result = info.windSpeed;
                    break;
                case "condition":
                    result = info.condition;
                    break;
                case "pressure":
                    result = info.pressure;
                    break;
                case "humidity":
                    result = info.humidity;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }

            writer.println(result);
            writer.flush();

        } catch (IOException | JSONException e) {
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
