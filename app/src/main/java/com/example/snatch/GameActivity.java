package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private ImageView[] bribes, thieves, policeCars;
    private Button game_BTN_left_arrow;
    private Button game_BTN_right_arrow;

    private final int LEFT_BOUNDARY = -1;
    private final int RIGHT_BOUNDARY = 3;
    private final int LOWER_BOUNDARY = 17;
    private final int DEFAULT_LIVES = 2;
    private final int DEFAULT_POSITION = 1;
    private final int DEFAULT_LANE_NUM = 3;

    private int thiefPosition;
    private int lives;
    private int speed = 350;
    private int policePosition1;
    private int policePosition2;
    private boolean isGameOver = false;
    private boolean isPaused = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setBribes();
        setThieves();
        setPoliceCars();
        setLives(DEFAULT_LIVES);
        setThiefPosition(DEFAULT_POSITION);

        game_BTN_right_arrow = findViewById(R.id.game_right_arrow);
        game_BTN_left_arrow = findViewById(R.id.game_left_arrow);

        /* Movement of thieves */
        game_BTN_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveThief(game_BTN_right_arrow.getContentDescription().toString());
            }
        });
        game_BTN_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveThief(game_BTN_left_arrow.getContentDescription().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(GameActivity.this, "The Cops Are Coming! RUN!", Toast.LENGTH_LONG).show();
        setRandomPoliceCars();
        isPaused = false;
        startGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntent = new Intent(GameActivity.this, MenuActivity.class);
        GameActivity.this.startActivity(mainIntent);
        isGameOver = true;
        GameActivity.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    //          Setters          //
    /* Setting ImageView's for bribes/lives */
    public void setBribes() {
        this.bribes = new ImageView[]{
                findViewById(R.id.game_bribe_1),
                findViewById(R.id.game_bribe_2),
                findViewById(R.id.game_bribe_3)
        };
    }

    /* Setting ImageView's for thieves */
    public void setThieves() {
        this.thieves = new ImageView[]{
                findViewById(R.id.game_thief_1),
                findViewById(R.id.game_thief_2),
                findViewById(R.id.game_thief_3)
        };
    }

    /* Setting ImageView's for police cars */
    public void setPoliceCars() {
        this.policeCars = new ImageView[]{
                findViewById(R.id.game_police_car_00), findViewById(R.id.game_police_car_01),
                findViewById(R.id.game_police_car_02), findViewById(R.id.game_police_car_10),
                findViewById(R.id.game_police_car_11), findViewById(R.id.game_police_car_12),
                findViewById(R.id.game_police_car_20), findViewById(R.id.game_police_car_21),
                findViewById(R.id.game_police_car_22), findViewById(R.id.game_police_car_30),
                findViewById(R.id.game_police_car_31), findViewById(R.id.game_police_car_32),
                findViewById(R.id.game_police_car_40), findViewById(R.id.game_police_car_41),
                findViewById(R.id.game_police_car_42), findViewById(R.id.game_police_car_50),
                findViewById(R.id.game_police_car_51), findViewById(R.id.game_police_car_52)
        };
    }

    /* Setting amount of bribes/lives */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /* Setting default position for thief */
    public void setThiefPosition(int position) {
        this.thiefPosition = position;
    }

    /* Police positions */
    public void setPolicePosition1(int pos) {
        this.policePosition1 = pos;
    }

    public void setPolicePosition2(int pos) {
        this.policePosition2 = pos;
    }


    //          Getters          //
    /* Get thief position */
    public int getThiefPosition() {
        return this.thiefPosition;
    }

    /* Get police car position */
    public int getPolicePosition1() {
        return this.policePosition1;
    }

    public int getPolicePosition2() {
        return this.policePosition2;
    }

    /* Get num of lives left */
    public int getNumOfLives() {
        return this.lives;
    }


    /* Thief movement function */
    private void moveThief(String direction) {
        switch (direction) {
            case "Right":
                if ((getThiefPosition() + 1) < RIGHT_BOUNDARY) {
                    thieves[getThiefPosition()].setVisibility(View.INVISIBLE);
                    setThiefPosition(getThiefPosition() + 1);
                    thieves[getThiefPosition()].setVisibility(View.VISIBLE);
                }
                break;

            case "Left":
                if ((getThiefPosition() - 1) > LEFT_BOUNDARY) {
                    thieves[getThiefPosition()].setVisibility(View.INVISIBLE);
                    setThiefPosition(getThiefPosition() - 1);
                    thieves[getThiefPosition()].setVisibility(View.VISIBLE);
                }
                break;
        }

    }


    private void startGame() {
        final Handler handler = new Handler();
        final Runnable myRun = new Runnable() {
            @Override
            public void run() {
                movePolice();
                checkIfBusted(getThiefPosition(), getPolicePosition1(), getPolicePosition2());
                checkIfLost();
                if (isGameOver) {
                    return;
                }
                if (isPaused) {
                    return;
                }
                startGame();
            }
        };
        handler.postDelayed(myRun, speed);
    }

    private void setRandomPoliceCars() {
        setPolicePosition1(MySignal.generateRandomInt(DEFAULT_LANE_NUM));
        setPolicePosition2(MySignal.generateRandomInt(DEFAULT_LANE_NUM));
        policeCars[getPolicePosition1()].setVisibility(View.VISIBLE);
        policeCars[getPolicePosition2()].setVisibility(View.VISIBLE);
    }

    private void movePolice() {
        if ((getPolicePosition1() + 3) <= LOWER_BOUNDARY && (getPolicePosition2() + 3) <= LOWER_BOUNDARY) {
            policeCars[getPolicePosition1()].setVisibility(View.INVISIBLE);
            policeCars[getPolicePosition2()].setVisibility(View.INVISIBLE);
            setPolicePosition1(getPolicePosition1() + 3);
            setPolicePosition2(getPolicePosition2() + 3);
            policeCars[getPolicePosition1()].setVisibility(View.VISIBLE);
            policeCars[getPolicePosition2()].setVisibility(View.VISIBLE);
        } else {
            clearBottom();
            setRandomPoliceCars();
        }
    }

    /* Clears the bottom row */
    public void clearBottom() {
        for (int i = 15; i <= LOWER_BOUNDARY; i++) {
            policeCars[i].setVisibility(View.INVISIBLE);
        }
    }

    /* Check if police busted thief */
    public void checkIfBusted(int thiefPosition, int policePosition1, int policePosition2) {
        if ((thiefPosition == 0 && (policePosition1 == 15 || policePosition2 == 15))
                || (thiefPosition == 1 && (policePosition1 == 16 || policePosition2 == 16))
                || (thiefPosition == 2 && (policePosition1 == 17 || policePosition2 == 17))) {
            MySignal.vibrate(GameActivity.this, 500);
            bribes[getNumOfLives()].setVisibility(View.INVISIBLE);
            setLives(getNumOfLives() - 1);
        }
    }

    /* Check if lost */
    public void checkIfLost() {
        if (getNumOfLives() == -1) {
            isGameOver = true;
            Intent mainIntent = new Intent(GameActivity.this, GameOverActivity.class);
            GameActivity.this.startActivity(mainIntent);
            GameActivity.this.finish();
        }
    }

}
