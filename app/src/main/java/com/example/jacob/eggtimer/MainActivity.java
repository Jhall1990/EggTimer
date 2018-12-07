package com.example.jacob.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    SeekBar eggTimeSeeker;
    int seekStep = 15;
    int timerMaxMinutes = 60;
    CountDownTimer eggTimer;

    public void startEggTimer(View view) {
        // Get the start button and disable it after it's pressed.
        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(false);

        // Get the stop button and make it visible.
        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setVisibility(View.VISIBLE);

        // Disable the seek bar.
        this.eggTimeSeeker.setEnabled(false);

        // Get the current time in seconds and create a new count down timer using it.
        int currentTimerSeconds = this.eggTimeSeeker.getProgress() * this.seekStep * 1000;

        // Create the egg timer which ticks every second. Each tick updates the
        // text view with the currently remaining time. If the timer finishes
        // an air horn sound is played.
        this.eggTimer = new CountDownTimer(currentTimerSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MainActivity.this.updateTime((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                MainActivity.this.updateTime(0);
                MainActivity.this.playSound();

                // Get the stop button and make it invisible.
                Button stopButton = findViewById(R.id.stopButton);
                stopButton.setVisibility(View.GONE);

                // Get the start button and re-enable it.
                Button startButton = findViewById(R.id.startButton);
                startButton.setEnabled(true);

                // Enable the seek bar and set the progress back to 0.
                MainActivity.this.eggTimeSeeker.setEnabled(true);
                MainActivity.this.eggTimeSeeker.setProgress(0);
            }
        };

        this.eggTimer.start();
    }

    public void stopEggTimer(View view) {
        // Stop the currently running timer.
        this.eggTimer.cancel();

        // Get the stop button and make it invisible.
        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setVisibility(View.GONE);

        // Get the start button and re-enable it.
        Button startButton = findViewById(R.id.startButton);
        startButton.setEnabled(true);

        // Enable the seek bar and set the progress back to 0.
        this.eggTimeSeeker.setEnabled(true);
        this.eggTimeSeeker.setProgress(0);

    }

    private void playSound() {
        MediaPlayer player = MediaPlayer.create(this, R.raw.air_horn);
        player.start();
    }

    private void updateTime(int totalSeconds) {
        // Gets the timer text view and sets it to the current time.
        TextView timerText = findViewById(R.id.eggTimeTextView);
        timerText.setText(this.convertSecondsToTimeString(totalSeconds));
    }

    private String convertSecondsToTimeString(int totalSeconds) {
        // Get the number of full hours.
        int hours = totalSeconds / 3600;
        totalSeconds -= hours * 3600;

        // Get the total remaining number of full minutes.
        int minutes = totalSeconds / 60;
        totalSeconds -= minutes * 60;

        // Get the remaining number of seconds.
        int seconds = totalSeconds;

        // Convert the time ints to strings.
        String strHour = String.format(Locale.getDefault(), "%02d", hours);
        String strMin = String.format(Locale.getDefault(), "%02d", minutes);
        String strSec = String.format(Locale.getDefault(), "%02d", seconds);

        // Create the timeText string based on the number of hours. If hours > 0
        // add it to the string, otherwise just use minutes and seconds.
        if (hours > 0) {
            return strHour + ":" + strMin + ":" + strSec;
        } else {
            return strMin + ":" + strSec;
        }
    }

    private void setSeekListener() {
        this.eggTimeSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.this.updateTime(progress * MainActivity.this.seekStep);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the egg time seeker.
        this.eggTimeSeeker = findViewById(R.id.eggTimerSeeker);

        // Set the egg timer max. Max is based on the max minutes divided by seekStep.
        this.eggTimeSeeker.setMax((this.timerMaxMinutes * 60) / this.seekStep);

        // Add a listener to the seek bar.
        this.setSeekListener();
    }
}
