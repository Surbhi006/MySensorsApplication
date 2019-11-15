package com.example.mysensorsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "";
    private SensorManager sensorManager;
    Sensor accelerometer;
    TextView xVal,yVal,zVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        xVal = findViewById(R.id.xValue);
        yVal = findViewById(R.id.yValue);
        zVal = findViewById(R.id.zValue);
        Log.d(TAG,"OnCreate:Intializing Sensor Services");
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener( AccelerometerActivity.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG,"OnCreate:Registered accelerometer listener");

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d(TAG,"OnSensorChanged: X: "+ event.values[0]+ " Y: "+event.values[1]);
        xVal.setText("xCoord: "+event.values[0]);
        yVal.setText("yCoord: "+event.values[1]);
        zVal.setText("zCoord: "+event.values[2]);
    }

}

