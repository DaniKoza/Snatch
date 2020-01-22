package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private final int RUNNER_DISPLAY_LENGTH = 2000;
    private ImageView splash_IMG_runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_main);

        splash_IMG_runner = findViewById(R.id.splash_IMG_runner);
        animateRunner();

        /* Handler to start the Menu-Activity
         * and close this Splash-Screen after 2 seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void animateRunner() {
        TranslateAnimation animation = new TranslateAnimation(-600.0f, 1050.0f, 0.0f, 0.0f);
        animation.setDuration(RUNNER_DISPLAY_LENGTH);
        animation.setFillAfter(false);
        splash_IMG_runner.startAnimation(animation);
    }
}

