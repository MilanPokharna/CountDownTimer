package com.android.countdowntimer;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class Main3Activity extends AppCompatActivity {
    TextView timer;
    public Long endtime,timeleft;
    public CountDownTimer countdowntimer;
    public Boolean mtimerunning=false;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("quiz");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        timer = (TextView)findViewById(R.id.counttimer);

    }

    private void startTimer() {
        endtime = System.currentTimeMillis() + timeleft;
        countdowntimer = new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long l) {
                timeleft = l;
                update();
            }

            @Override
            public void onFinish() {
                mtimerunning = false;
                timeleft = Long.valueOf(0);
                update();
            }
        }.start();
        mtimerunning =true;
    }

    private void update() {
        int hour = (int) (timeleft/1000)/60/60;
        int min = (int) (timeleft/1000)/60;
        int sec = (int) (timeleft/1000)%60;

        String t = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,min,sec);
        timer.setText(t);
    }
}
