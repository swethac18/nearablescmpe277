package com.estimote.notification;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.notification.estimote.NotificationsManager;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

/**
 * Author : Swetha Chandrasekar
 */
public class MainActivity extends AppCompatActivity {

    Button trackButton;
    TextView distanceTextView;
    public static boolean isTracking = false;



    public void onClickTrackButton(View view){
        if(isTracking){
            isTracking = false;
            if (NotificationsManager.mediaPlayer != null) {
                NotificationsManager.mediaPlayer.stop();
                NotificationsManager.mediaPlayer.setLooping(false);

                if (NotificationsManager.mediaPlayer.isPlaying()) {
                    NotificationsManager.mediaPlayer.setLooping(false);

                    NotificationsManager.mediaPlayer.stop();
                }
                NotificationsManager.mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm_sound);
            }
            trackButton.setText("Start Tracking!");
            Tracker4Pet application = (Tracker4Pet) getApplication();
            application.getNotificiationsManager().stopMonitoring();
            trackButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        }
        else{
            isTracking = true;
            String distanceString = distanceTextView.getText().toString();
            distanceString = distanceString.replace('m', ' ').trim();
            if (distanceString.contains("sleep")) {
                distanceString = "5";
            } else if (distanceString.contains("medium")) {
                distanceString = "8";
            } else {
                distanceString = "10";
            }
            try {
                Float distance = Float.parseFloat(distanceString);
                Tracker4Pet application = (Tracker4Pet) getApplication();
                application.getNotificiationsManager().startMonitoring(distance);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("issue in parsing distance string");
            }
            trackButton.setText("Stop Tracking!");
            trackButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void onClickAnalytics(View view){

        Log.i("onClick","Analytics");
        Intent intent = new Intent(MainActivity.this, LostAndFoundActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Tracker4Pet application = (Tracker4Pet) getApplication();

        final double step = 0.1;
        final int min = 0 ;
        int max = 15;

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        trackButton = (Button)findViewById(R.id.trackButton);
        distanceTextView = findViewById(R.id.distanceTextView);

        NotificationsManager.mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm_sound);

        seekBar.setMax((int) ((max-min)/step));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Double value = min + (progress * step);
                Log.i("distance", String.valueOf(value));
                String st = value.toString();
                if (st.length() > 3) {
                    st = st.substring(0,3);
                }
                if (value < 5) {
                    st = "sensitive short distance (sleep mode)";
                } else if (value < 10) {
                    st = "sensitive medium distance (a few meters)";
                } else {
                    st = "track if object is away by more than 8 meters (suited for playtime)";
                }
                distanceTextView.setText(st);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                application.enableBeaconNotifications();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });
    }

    public static void drawMap(){

    }

}
