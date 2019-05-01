package com.example.onlineattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {


    TextView role_tv;
    ImageView logo_iv;
    Animation down_from_top, up_from_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        role_tv = findViewById(R.id.role_tv);
        logo_iv = findViewById(R.id.logo_iv);
        down_from_top = AnimationUtils.loadAnimation(this, R.anim.down_frpm_top);
        up_from_bottom = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);


        logo_iv.setAnimation(up_from_bottom);
        role_tv.setAnimation(up_from_bottom);



        int timeout = 3000;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Done ");
                finish();
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, timeout);
    }
}
