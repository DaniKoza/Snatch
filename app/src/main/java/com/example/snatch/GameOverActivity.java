package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOverActivity extends AppCompatActivity {

    private Button BTN_play_again;
    private Button BTN_back_to_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        BTN_play_again = findViewById(R.id.BTN_play_again);
        BTN_back_to_main = findViewById(R.id.BTN_back_to_main);

        /* Play again */
        BTN_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(GameOverActivity.this, GameActivity.class);
                GameOverActivity.this.startActivity(mainIntent);
                GameOverActivity.this.finish();
            }
        });


        /* Go back to main menu */
        BTN_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(GameOverActivity.this, MenuActivity.class);
                GameOverActivity.this.startActivity(mainIntent);
                GameOverActivity.this.finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(GameOverActivity.this, MenuActivity.class);
        startActivity(mainIntent);
        finish();
    }

}
