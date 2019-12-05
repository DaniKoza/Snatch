package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private Button menu_BTN_play;
    private Button menu_BTN_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menu_BTN_play = findViewById(R.id.btn_play);
        menu_BTN_settings = findViewById(R.id.btn_settings);

        //ToDo: settings menu
        menu_BTN_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        /* Go to game */
        menu_BTN_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MenuActivity.this, GameActivity.class);
                MenuActivity.this.startActivity(mainIntent);
            }
        });

    }
}
