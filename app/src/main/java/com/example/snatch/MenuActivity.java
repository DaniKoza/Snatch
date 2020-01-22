package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button menu_BTN_play;
    private Button menu_BTN_settings;
    private Button menu_BTN_best_scores;
    private DollarAnimationView mAnimationView;
    private MyLocation mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu_BTN_play = findViewById(R.id.btn_play);
        menu_BTN_settings = findViewById(R.id.btn_settings);
        menu_BTN_best_scores = findViewById(R.id.btn_best_scores);
        mAnimationView = findViewById(R.id.animated_dollars_view);
        mAnimationView.setVisibility(View.VISIBLE); // for easier layout edit, its invisible by default.

        //Start collect location data
        mLocation = new MyLocation(this);

        menu_BTN_settings.setOnClickListener(v -> {
            mAnimationView.pause();
            Intent mainIntent = new Intent(MenuActivity.this, SettingsActivity.class);
            MenuActivity.this.startActivity(mainIntent);
        });

        /* Go to game */
        menu_BTN_play.setOnClickListener(v -> {
            mAnimationView.pause();
            Intent mainIntent = new Intent(MenuActivity.this, GameActivity.class);
            MenuActivity.this.startActivity(mainIntent);
        });

        menu_BTN_best_scores.setOnClickListener(v -> {
            mAnimationView.pause();
            Intent mainIntent = new Intent(MenuActivity.this, MapsActivity.class);
            MenuActivity.this.startActivity(mainIntent);
        });

        statusCheck();

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAnimationView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();mLocation = new MyLocation(this);
        mAnimationView.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAnimationView.pause();
        this.finish();
    }
}
