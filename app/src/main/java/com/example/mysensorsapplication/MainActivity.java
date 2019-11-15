package com.example.mysensorsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button accButton,WifiButton,GpsButton,MicrophoneButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDB();
        accButton = findViewById(R.id.accelerometer);
        WifiButton =findViewById(R.id.wifi);
        GpsButton = findViewById(R.id.gps);
        MicrophoneButton = findViewById(R.id.microphone);

        accButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accIntent = new Intent(getApplicationContext(),AccelerometerActivity.class);
                startActivity(accIntent);
            }
        });
        WifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wifiIntent = new Intent(getApplicationContext(),WifiActivity.class);
                startActivity(wifiIntent);
            }
        });
        GpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gpsintent = new Intent(getApplicationContext(),GpsActivity.class);
                startActivity(gpsintent);
            }
        });
        MicrophoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent microphoneintent = new Intent(getApplicationContext(),MicrophoneActivity.class);
                startActivity(microphoneintent);
            }
        });
    }

    public void createDB() {
        new DBManager(this);
    }
}
