package com.android.countdowntimer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Main3Activity extends AppCompatActivity {
    TextView timer;
    public Long endtime= Long.valueOf(0),timeleft= Long.valueOf(00);
    String sdate="",sd="";
    public CountDownTimer countdowntimer;
    public Boolean mtimerunning=false;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("quiz");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        timer = (TextView)findViewById(R.id.counttimer);
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
//        timeleft = prefs.getLong("timeleft",0);
        sd= prefs.getString("sd","0");
        mtimerunning = prefs.getBoolean("mtimerunning",false);

        reference.child("1").child("quiztime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sdate = dataSnapshot.getValue().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                //Toast.makeText(Main3Activity.this, "time "+sdate, Toast.LENGTH_SHORT).show();
                if(!sd.equals(sdate))
                {
                    if (countdowntimer!=null)
                        countdowntimer.cancel();
                    sd=sdate;
                    try{
                        //formatting the dateString to convert it into a Date
                        Date date = sdf.parse(sdate);
                        if (date.getTime() < System.currentTimeMillis())
                        {
                            //Toast.makeText(Main3Activity.this, "Quiz Time Gone", Toast.LENGTH_SHORT).show();
                            timer.setText("Quiz has been Started");
                            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("mtimerunning",false);
                        }
                        else {
                            timeleft = date.getTime() - System.currentTimeMillis();
                            mtimerunning =true;
                            SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putLong("timeleft",timeleft);
                            editor.putBoolean("mtimerunning",mtimerunning);
                            editor.putLong("endtime",0);
                            editor.putString("sd",sd);
                            editor.apply();
                            starttimer();
                        }

                        //Setting the Calendar date and time to the given date and time
                    }catch(ParseException e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void starttimer() {
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
                if(countdowntimer!=null)
                    countdowntimer.cancel();
                //Toast.makeText(Main3Activity.this, "finish", Toast.LENGTH_SHORT).show();
                timer.setText("Quiz has been Started");
                timeleft= Long.valueOf(0);
                endtime= Long.valueOf(0);
            }
        }.start();
    }

    private void update() {
//        int hour = (int) ((timeleft/1000)/60/60)%60;
        final int m = (int)((timeleft/1000/60))%60;
//        int min = (int) (timeleft/1000)/60;
        int sec = (int) (timeleft/1000)%60;

        int hour = (int) TimeUnit.MILLISECONDS.toHours(timeleft);

        String t = String.format(Locale.getDefault(),"%02dh",hour);
//        String t2 = String.format(Locale.getDefault(),"%02d",min);
        String t2 = String.format(Locale.getDefault(),"%02d",m);
        String t3 = String.format(Locale.getDefault(),"%02ds",sec);
        timer.setText(t+":"+t2+":"+t3);
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timeleft",timeleft);
        editor.putBoolean("mtimerunning",mtimerunning);
        editor.putLong("endtime",endtime);
        editor.putString("sd",sd);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        timeleft = prefs.getLong("timeleft",0);
        mtimerunning = prefs.getBoolean("mtimerunning",false);

        if (mtimerunning)
        {
            endtime = prefs.getLong("endtime",0);
            timeleft = endtime - System.currentTimeMillis();
            if (timeleft < 0) {
                mtimerunning = false;
                timeleft = Long.valueOf(0);
                //Toast.makeText(this, "on start", Toast.LENGTH_SHORT).show();
                timer.setText("Quiz has been Started");
            }
            else
                starttimer();
        }
        else
            timer.setText("Quiz has been Started");
    }

}
