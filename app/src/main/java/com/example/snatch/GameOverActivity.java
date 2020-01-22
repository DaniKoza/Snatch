package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class GameOverActivity extends AppCompatActivity {

    private Button BTN_play_again;
    private Button BTN_back_to_main;
    private Button BTN_high_scores;
    private Button game_over_BTN_save;
    private TextView game_over_score;

    private int playerScore;
    private String playerName;
    private ArrayList<Record> highScoreList;
    private MySharedPreferences msp;
    private double latitude, longitude;

    private MediaPlayer sound_game_over;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        highScoreList = new ArrayList<>();
        BTN_play_again = findViewById(R.id.BTN_play_again);
        BTN_back_to_main = findViewById(R.id.BTN_back_to_main);
        BTN_high_scores = findViewById(R.id.BTN_high_scores);
        game_over_BTN_save = findViewById(R.id.game_over_BTN_save);
        game_over_score = findViewById(R.id.game_over_player_score);
        sound_game_over = MediaPlayer.create(GameOverActivity.this, R.raw.game_over);


        //Get the player's score from the game activity by the intent
        Intent intent = getIntent();
        playerScore = getScoreFromIntent(intent);
        game_over_score.setText("Your score is " + playerScore + "points");


        /* Play again */
        BTN_play_again.setOnClickListener(v -> {
            Intent mainIntent = new Intent(GameOverActivity.this, GameActivity.class);
            GameOverActivity.this.startActivity(mainIntent);
            GameOverActivity.this.finish();
        });


        /* Go back to main menu */
        BTN_back_to_main.setOnClickListener(v -> GameOverActivity.this.finish());

        BTN_high_scores.setOnClickListener(v -> {
            Intent mainIntent = new Intent(GameOverActivity.this, MapsActivity.class);
            GameOverActivity.this.startActivity(mainIntent);
            GameOverActivity.this.finish();
        });

        game_over_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlayerName();
            }
        });

        //Check if the player's score is high enough for save
        if (isOKtoSave()) {
            changeButtonConfig(game_over_BTN_save, true);
            if (highScoreList.size() == 10)
                highScoreList.remove(highScoreList.get(highScoreList.size() - 1));
        } else {
            changeButtonConfig(game_over_BTN_save, false);
            Toast.makeText(this, "Your score CANNOT enter the record list!", Toast.LENGTH_SHORT).show();
        }

        sound_game_over.seekTo(0);
        sound_game_over.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public int getScoreFromIntent(Intent intent) {
        return intent.getIntExtra("score", 0);
    }

    private void getSoreListFromShared() {
        highScoreList = msp.getArrayList("scoreList", "na");
    }

    private void getPlayerLocationFromShared() {
        msp = new MySharedPreferences(this);
        latitude = (double) msp.getFloat("playerLatitude", 0f);
        longitude = (double) msp.getFloat("playerLongitude", 0f);
    }

    private boolean isOKtoSave() {
        getPlayerLocationFromShared();
        getSoreListFromShared();
        if (highScoreList.isEmpty() || highScoreList.size() < 10) {
            return true;
        } else {
            Record lastRecord = getTheLastRecord();
            return playerScore - lastRecord.getScore() > 0;
        }
    }

    private Record getTheLastRecord() {
        return highScoreList.get(highScoreList.size() - 1);

    }

    private void changeButtonConfig(Button btn, boolean shouldBeEnable) {
        if (shouldBeEnable) {
            btn.setAlpha(1);
            btn.setClickable(true);
        } else {
            btn.setAlpha(0.5f);
            btn.setClickable(false);
        }
    }

    public void getPlayerName() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerName = input.getText().toString();
                if (playerName.isEmpty()) {
                    //Notify the player that he cannot save score with no name
                    Toast.makeText(GameOverActivity.this, "Name is undefined", Toast.LENGTH_SHORT).show();
                } else {
                    highScoreList.add(new Record(playerName, playerScore, latitude, longitude));
                    changeButtonConfig(game_over_BTN_save, false);
                    changeButtonConfig(game_over_BTN_save, true);
                    sortAndSaveScoreList();
                }
            }
        });
        builder.show();
    }

    private void sortAndSaveScoreList() {
        Collections.sort(highScoreList);
        //solve the problem with Json and raw type on record class
        msp.putArrayList(highScoreList);
    }
}
