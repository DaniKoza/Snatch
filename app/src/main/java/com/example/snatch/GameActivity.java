package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    private final int DEFAULT_LIVES = 2;
    private final int DEFAULT_SCORE = 0;
    private final int DEFAULT_POLICE_INTERVAL = 5;
    private final int DEFAULT_POSITION = 22;

    private final int COLUMNS = 5;
    private final int ROWS = 5;

    private ImageView[] bribes;
    private ImageView[] grid = new ImageView[COLUMNS * ROWS];
    private int[] figures = {R.drawable.thief, R.drawable.police_car, R.drawable.coin};
    private Button game_BTN_left_arrow;
    private Button game_BTN_right_arrow;
    private GridLayout game_LAYOUT_grid;

    private int thiefCurrentPos;
    private int lives;
    private int score;
    private int intervalBetweenNewPolice;
    private int speed;
    private Handler handler = new Handler();
    private Runnable myRun;


    private MySharedPreferences msp;
    private MyRotation mrs;
    private MediaPlayer sound_coin;
    private MediaPlayer sound_busted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        msp = new MySharedPreferences(this);
        setBribesViews();
        setGridViews();
        setArrowsViews();
        setDefaultData();
        setControlSettings();
        startGame();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(GameActivity.this, "The Cops Are Coming! RUN!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mrs != null)
            mrs.setSensor();
        handler.postDelayed(myRun, speed);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mrs != null)
            mrs.stopSensor();
        handler.removeCallbacks(myRun);
    }


    //          Setters          //
    /* Setting ImageView's for bribes/lives */
    public void setBribesViews() {
        this.bribes = new ImageView[]{
                findViewById(R.id.game_bribe_1),
                findViewById(R.id.game_bribe_2),
                findViewById(R.id.game_bribe_3)
        };
    }

    private void setGridViews() {
        game_LAYOUT_grid = findViewById(R.id.game_LAYOUT_grid);
        thiefCurrentPos = DEFAULT_POSITION;
        for (int i = 0; i < grid.length; i++) {
            grid[i] = (ImageView) game_LAYOUT_grid.getChildAt(i);
            if (i == DEFAULT_POSITION) {
                grid[i].setImageResource(R.drawable.thief);
            }
        }
    }

    /* Setting ImageView's for buttons */
    private void setArrowsViews() {
        game_BTN_right_arrow = findViewById(R.id.game_right_arrow);
        game_BTN_left_arrow = findViewById(R.id.game_left_arrow);
    }

    /* Setting amount of bribes/lives */
    public void setDefaultData() {
        this.lives = DEFAULT_LIVES;
        this.score = DEFAULT_SCORE;
        this.intervalBetweenNewPolice = DEFAULT_POLICE_INTERVAL;
        speed = msp.getInt("speed", 1000);

        sound_busted = MediaPlayer.create(GameActivity.this, R.raw.busted);
        sound_coin = MediaPlayer.create(GameActivity.this, R.raw.coin);
    }

    /* Setting type of control - sensors/arrows */
    private void setControlSettings() {
        String controlType = msp.getString("control", "A");
        if (controlType.equals("A")) {
            setArrowsAction();
        } else {
            mrs = new MyRotation(GameActivity.this, cbInterface);
            mrs.setSensor();
            game_BTN_left_arrow.setAlpha(0);
            game_BTN_right_arrow.setAlpha(0);
        }
    }


    /* Thief movement functions*/
    /* Callback interface for moving the thief with either arrows or the rotation sensors */
    CallBackInterface cbInterface = new CallBackInterface() {
        @Override
        public void moveRight() {
            if (thiefCurrentPos < DEFAULT_POSITION + 2) {
                //Checks if there is a police in the RIGHT side of the car to determine collision
                if (grid[thiefCurrentPos + 1].getDrawable() != null) {
                    if (typeOfObjectAhead(grid[thiefCurrentPos + 1], "police")) {
                        busted(grid[thiefCurrentPos + 1]);
                    } else if (typeOfObjectAhead(grid[thiefCurrentPos + 1], "coin")) {
                        coinPickUp(grid[thiefCurrentPos + 1]);
                    }
                }
                moveThiefRight();
            }
        }

        @Override
        public void moveLeft() {
            if (thiefCurrentPos > DEFAULT_POSITION - 2) {
                //Checks if there is a police in the LEFT side of the car to determine collision
                if (grid[thiefCurrentPos - 1].getDrawable() != null) {
                    if (typeOfObjectAhead(grid[thiefCurrentPos - 1], "police")) {
                        busted(grid[thiefCurrentPos - 1]);
                    } else if (typeOfObjectAhead(grid[thiefCurrentPos - 1], "coin")) {
                        coinPickUp(grid[thiefCurrentPos - 1]);
                    }
                }
                moveThiefLeft();
            }
        }
    };

    // Arrows movement action
    private void setArrowsAction() {
        game_BTN_right_arrow.setOnClickListener(v -> cbInterface.moveRight());
        game_BTN_left_arrow.setOnClickListener(v -> cbInterface.moveLeft());
    }

    /* Move the thief to the left in the grid */
    public void moveThiefLeft() {
        grid[thiefCurrentPos].setImageResource(0);
        thiefCurrentPos--;
        grid[thiefCurrentPos].setImageResource(R.drawable.thief);
    }

    /* Move the thief to the right in the grid */
    public void moveThiefRight() {
        grid[thiefCurrentPos].setImageResource(0);
        thiefCurrentPos++;
        grid[thiefCurrentPos].setImageResource(R.drawable.thief);
    }

    //## END OF THIEF MOVEMENT FUNCTIONS ##//

    // Start the handler to run the game
    private void startGame() {
        myRun = () -> {
            gameLoop();
            startGame();

        };
        handler.postDelayed(myRun, speed);
    }


    // Game action
    private void gameLoop() {
        for (int i = grid.length - 1; i > 4; i--) {
            //Remove all the polices that already pass the car
            if (i > 17) {
                grid[i].setImageResource(0);
                grid[thiefCurrentPos].setImageResource(R.drawable.thief);
            }
            //If there is an non-empty ImageView(police/coin), we Compare that ImageView with the ImageView in row ahead to determinate collision OR bonus
            if ((grid[i - COLUMNS].getDrawable() != null)) {
                if (grid[i].getDrawable() == null) {
                    //move the Police OR the coin 1 row forward
                    if (typeOfObjectAhead(grid[i - COLUMNS], "police"))
                        setImageViewInGrid(grid[i - COLUMNS], figures[1]);

                    else if (typeOfObjectAhead(grid[i - COLUMNS], "coin")) {
                        setImageViewInGrid(grid[i - COLUMNS], figures[2]);
                    }
                } else if (grid[i].getDrawable() != null) {
                    //Collision OR bonus
                    if (typeOfObjectAhead(grid[i - COLUMNS], "police")) {
                        busted(grid[i - COLUMNS]);
                    } else if (typeOfObjectAhead(grid[i - COLUMNS], "coin")) {
                        coinPickUp(grid[i - COLUMNS]);
                    }
                }
            }
        }

        if (score % 20 == 0)
            launchObstacle(2);
        //Draw new police on the screen
        if (score % intervalBetweenNewPolice == 0) {
            launchObstacle(1);
        }
        score += 5;
    }


    //  Picks a Random location and sets a figure
    public void launchObstacle(int fig) {
        int newLocation = (int) (Math.random() * COLUMNS);
        grid[newLocation].setImageResource(figures[fig]);
    }

    // Helps set the right resources
    private void setImageViewInGrid(ImageView _grid, int resID) {
        int index = Arrays.asList(grid).indexOf(_grid);
        grid[index + COLUMNS].setImageResource(resID);
        grid[index].setImageResource(0);
    }


    /* Check if thief caught a coin or got busted */
    public boolean typeOfObjectAhead(ImageView obstacle, String nameOfObstacle) {
        //Create bitmap for the object image we want to move forward
        Bitmap objectBitmap = ((BitmapDrawable) obstacle.getDrawable()).getBitmap();

        if (nameOfObstacle.equalsIgnoreCase("police")) {
            //Create bitmap for the police resource image
            Drawable policeResource = getResources().getDrawable(R.drawable.police_car);
            Bitmap policeResourceBitmap = ((BitmapDrawable) policeResource).getBitmap();

            return objectBitmap.sameAs(policeResourceBitmap);

        } else if (nameOfObstacle.equalsIgnoreCase("coin")) {
            //Create bitmap for the coin resource image
            Drawable coinResource = getResources().getDrawable(R.drawable.coin);
            Bitmap coinResourceBitmap = ((BitmapDrawable) coinResource).getBitmap();

            return objectBitmap.sameAs(coinResourceBitmap);
        } else return false;
    }

    // When thief gets busted //
    public void busted(ImageView policeCar) {
        sound_busted.seekTo(0);
        sound_busted.start();
        lives--;
        MySignal.vibrate(getApplicationContext(), 300);
        policeCar.setImageResource(0);
        if (lives == -1)
            GameOver();
        else
            bribes[lives].setVisibility(View.INVISIBLE);
        policeCar.setImageResource(0);
    }

    // When thief gets a coin //
    private void coinPickUp(ImageView coin) {
        score += 10;
        coin.setImageResource(0);
        sound_coin.seekTo(0);
        sound_coin.start();
    }

    /* Game Over ! */
    public void GameOver() {
        handler.removeCallbacks(myRun);
        Intent mainIntent = new Intent(GameActivity.this, GameOverActivity.class);
        mainIntent.putExtra("score", score);
        GameActivity.this.startActivity(mainIntent);
        GameActivity.this.finish();

    }

}
