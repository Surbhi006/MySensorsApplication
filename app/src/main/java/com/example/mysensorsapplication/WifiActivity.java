package com.example.mysensorsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WifiActivity extends AppCompatActivity implements Serializable{

    WifiManager wifiManager;
    WifiReceiver wifiReceiver;
    Button exportButton;
    List<ScanResult> myWifiList;
    List<String> list;
    TextView textView;
    DBManager manager;
    ListView listView;
    public transient Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
//        textView = (TextView)findViewById(R.id.wifiList);
        listView = (ListView) findViewById(R.id.wifilist);
        exportButton = (Button) findViewById(R.id.export);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);

        } else {
            scanWifiList();
        }
        list = getPreviousWifiData();

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDataToFile(list);
            }
        });
    }

    public void exportDataToFile(List<String> list) {
        FileOutputStream fos = null;
            try {
                fos = openFileOutput("WifiList.txt", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(list);
                os.close();

            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());

            }
            System.out.println(getFilesDir()+"/WifiList.txt");
            Toast.makeText(this,"saved to"+getFilesDir()+"/WifiList.txt",Toast.LENGTH_SHORT).show();
    }

    public List<String> getPreviousWifiData() {
        List<String> list = new ArrayList<String>();
        manager = new DBManager(this);
        Cursor cursor = manager.getWifiData();
        if(cursor.getCount() == 0) {
            Toast.makeText(this,"NO DATA",Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String str = cursor.getString(0) +"  "+ cursor.getString(1) +" " +cursor.getString(2);
//                Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
                list.add(str);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.wifi_list_item, list);

        ListView listView = (ListView) findViewById(R.id.wifilist);
        listView.setAdapter(adapter);
        return list;
    }

    private void scanWifiList() {
        wifiManager.startScan();
        String names = "";
        long timestamp = 0;
        myWifiList = wifiManager.getScanResults();
        System.out.println(myWifiList.get(0).SSID);
        for (int i=0; i<myWifiList.size(); i++) {
            names+=myWifiList.get(i).SSID+"\n";
        }
        timestamp = myWifiList.get(0).timestamp;
        Date date = new Date(timestamp);
        insertIntoDB(names,date.toString());
    }

    public void insertIntoDB(String names , String timestamp) {
        DBManager dbManager = new DBManager(this);
        String res = dbManager.addWifiRecord(names,timestamp);
//        Toast.makeText(this,res,Toast.LENGTH_LONG).show();
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
