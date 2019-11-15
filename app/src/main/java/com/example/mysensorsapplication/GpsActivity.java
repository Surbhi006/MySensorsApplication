package com.example.mysensorsapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class GpsActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener listener;
    Context context;
    TextView tv;
    Button exportButton;
    DBManager manager;
    ListView listView;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        tv = (TextView) findViewById(R.id.textView);
//        listView = (ListView) findViewById(R.id.gpslist);
//        exportButton = (Button) findViewById(R.id.export);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                tv.append("\n" + location.getLatitude() + " " + location.getLongitude() + dateFormat.format(date));
//                list = getPreviousGpsData();
//                System.out.println(list);
                String loc = location.getLatitude()+" "+location.getLongitude();

                insertIntoDB(loc,dateFormat.format(date));
//                System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            configureButton();
        }

    }

    public List<String> getPreviousGpsData() {
        List<String> list = new ArrayList<String>();
        manager = new DBManager(this);
        Cursor cursor = manager.getWifiData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String str = cursor.getString(0) + "  " + cursor.getString(1) + " " + cursor.getString(2);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureButton();
                }
        }
    }

    private void configureButton() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return;
//            }
//        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }

    public void insertIntoDB(String loc , String timestamp) {
        DBManager dbManager = new DBManager(this);
        String res = dbManager.addGpsRecord(loc,timestamp);
//        Toast.makeText(this,res,Toast.LENGTH_LONG).show();
    }


}
