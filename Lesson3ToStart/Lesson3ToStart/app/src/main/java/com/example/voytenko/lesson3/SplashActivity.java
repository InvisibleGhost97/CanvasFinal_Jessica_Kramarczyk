package com.example.voytenko.lesson3;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // mp = MediaPlayer.create(this, R.raw.goat);
        //mp.start();

        //RUN SplashActivity in a seperate thread, wait for 2 seconds and terminate
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                finally {
                    Intent myIntent = new Intent(SplashActivity.this, Main.class);
                    startActivity(myIntent);

                }
            }
        };

        timer.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.finish();
    }

}
