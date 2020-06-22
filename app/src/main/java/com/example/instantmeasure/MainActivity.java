package com.example.instantmeasure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, String.valueOf(OpenCVLoader.initDebug()), Toast.LENGTH_LONG).show();
       Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(MainActivity.this, ActivityLogin.class);
                startActivity(intent);

                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                finish();
            }
        },2500);




    }
}
