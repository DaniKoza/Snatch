package com.example.snatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {
    private MySharedPreferences msp;
    private RadioButton settings_RBTN_fast;
    private RadioButton settings_RBTN_slow;
    private RadioButton settings_RBTN_buttons;
    private RadioButton settings_RBTN_sensors;
    private RadioButton settings_RBTN_sound_on;
    private RadioButton settings_RBTN_sound_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        msp = new MySharedPreferences(this);
        initRadioButtonsViews();

        settings_RBTN_fast.setOnClickListener(onRadioButtonClicked);
        settings_RBTN_slow.setOnClickListener(onRadioButtonClicked);
        settings_RBTN_buttons.setOnClickListener(onRadioButtonClicked);
        settings_RBTN_sensors.setOnClickListener(onRadioButtonClicked);
        settings_RBTN_sound_on.setOnClickListener(onRadioButtonClicked);
        settings_RBTN_sound_off.setOnClickListener(onRadioButtonClicked);

        rememberPreviousSettings();

    }

    /**
     * Putting Default settings and remembering old preference
     */
    private void rememberPreviousSettings() {
        if(msp.getInt("speed",1000) == 1000)
            settings_RBTN_slow.setChecked(true);
        else
            settings_RBTN_fast.setChecked(true);

        if(msp.getString("control","A").equals("A"))
            settings_RBTN_buttons.setChecked(true);
        else
            settings_RBTN_sensors.setChecked(true);

        if(msp.getString("sound","on").equals("on"))
            settings_RBTN_sound_on.setChecked(true);
        else
            settings_RBTN_sound_off.setChecked(true);

    }

    /**
     * Bind views and radio buttons
     */
    private void initRadioButtonsViews() {
        settings_RBTN_fast = findViewById(R.id.settings_RBTN_fast);
        settings_RBTN_slow = findViewById(R.id.settings_RBTN_slow);
        settings_RBTN_buttons = findViewById(R.id.settings_RBTN_buttons);
        settings_RBTN_sensors = findViewById(R.id.settings_RBTN_sensors);
        settings_RBTN_sound_on = findViewById(R.id.settings_RBTN_sound_on);
        settings_RBTN_sound_off = findViewById(R.id.settings_RBTN_sound_off);
    }


    /**
     * Saving and updating preferences according to radio buttons
     */
    View.OnClickListener onRadioButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Is the button now checked?
            boolean checked = ((RadioButton) v).isChecked();
            // Check which radio button was clicked
            switch (v.getId()) {
                case R.id.settings_RBTN_slow:
                    if (checked)
                        msp.putInt("speed", 1000);
                    break;
                case R.id.settings_RBTN_fast:
                    if (checked)
                        msp.putInt("speed", 600);
                    break;
                case R.id.settings_RBTN_buttons:
                    if (checked)
                        msp.putString("control", "A");
                    break;
                case R.id.settings_RBTN_sensors:
                    if (checked)
                        msp.putString("control", "S");
                    break;
                case R.id.settings_RBTN_sound_on:
                    if (checked)
                        msp.putString("sound", "on");
                    break;
                case R.id.settings_RBTN_sound_off:
                    if (checked)
                        msp.putString("sound", "off");
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
