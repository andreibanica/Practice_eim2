package ro.pub.cs.systems.eim.practice_eim2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText serverPort;
    Button connect;

    EditText address;
    EditText city;
    EditText clientPort;
    Button getWeather;
    Spinner types;

    TextView weatherView;

    Server server;
    Client client;

    ConnectButtonListener connectButtonListener =new ConnectButtonListener();
    private class ConnectButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String port = serverPort.getText().toString();
            server = new Server(Integer.parseInt(port));
            server.start();
        }
    }

    GetWeatherListener getWeatherListener = new GetWeatherListener();
    class GetWeatherListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String addr = address.getText().toString();
            String port = clientPort.getText().toString();
            String c = city.getText().toString();
            String type = types.getSelectedItem().toString();

            weatherView.setText("");

            client = new Client(addr, Integer.parseInt(port), c, type, weatherView);
            client.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPort = findViewById(R.id.server_edit);
        connect = findViewById(R.id.server_connect);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        clientPort = findViewById(R.id.client_edit);
        getWeather = findViewById(R.id.get_weather);
        types = findViewById(R.id.spinner);

        weatherView = findViewById(R.id.weather_view);

        connect.setOnClickListener(connectButtonListener);
        getWeather.setOnClickListener(getWeatherListener);
    }

    @Override
    protected void onDestroy() {
        if (server != null) {
            server.stopThread();
        }
        super.onDestroy();
    }
}
