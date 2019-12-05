package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class LandingActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_main);

        /* Handler to start the Menu-Activity
         * and close this Splash-Screen after 2 seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(LandingActivity.this, MenuActivity.class);
                LandingActivity.this.startActivity(mainIntent);
                LandingActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

