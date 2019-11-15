package com.example.mysensorsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MicrophoneActivity extends AppCompatActivity {

    Button recButton,stopRecButton,playButton,stopButton;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String pathSave = "";
    String path = "";

    final int REQUEST_PERMISSION_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone);
        recButton = (Button) findViewById(R.id.record);
        stopRecButton = (Button) findViewById(R.id.stoprecord);
        playButton = (Button) findViewById(R.id.play);
        stopButton = (Button) findViewById(R.id.stop);
        pathSave = getExternalCacheDir().getAbsolutePath();
        pathSave += "/audiorecordtest.3gp";

        if(!checkPermissionFromDevice()) {
            requestPermission();
        }


            recButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
//                            + UUID.randomUUID().toString()+"_audio_record.3gp";
                    if(checkPermissionFromDevice()) {
                        setupMediaRecorder();
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        playButton.setEnabled(false);
                        stopButton.setEnabled(false);

                        Toast.makeText(MicrophoneActivity.this,"recording.....",Toast.LENGTH_SHORT).show();
                    } else {
                        requestPermission();
                    }



                }
            });
            stopRecButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaRecorder.stop();
                    stopRecButton.setEnabled(false);
                    playButton.setEnabled(true);
                    recButton.setEnabled(true);
                    stopButton.setEnabled(false);
                }
            });
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopButton.setEnabled(true);
                    stopRecButton.setEnabled(false);
                    recButton.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(MicrophoneActivity.this,"playing....",Toast.LENGTH_SHORT).show();
                }
            });
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopButton.setEnabled(false);
                    stopRecButton.setEnabled(false);
                    recButton.setEnabled(true);
                    playButton.setEnabled(true);

                    if(mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();
                    }
                }
            });


    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"permission granted" , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"permission denied" , Toast.LENGTH_SHORT).show();
                }
            }

                break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);

        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }


}
