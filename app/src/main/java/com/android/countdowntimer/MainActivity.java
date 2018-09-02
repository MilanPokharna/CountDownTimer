package com.android.countdowntimer;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long starttime = 60000;
    TextView time;
    Button start;
    TextView selectedtime;
    Button select;
    private CountDownTimer countdowntimer;
    public Boolean mtimerunning = false;
    private long timeleft = starttime;
    private long endtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView);
        selectedtime= (TextView)findViewById(R.id.selecttime);
        update();
        start = (Button)findViewById(R.id.reset);
        select = (Button)findViewById(R.id.select);
        start.setBackgroundColor(Color.GREEN);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        int hour = selectedHour;
                        int minutes = selectedMinute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes ;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min ).append(" ").append(timeSet).toString();
                        selectedtime.setText(aTime);
                    }
                }, hours, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mtimerunning)
                    starttimer();
                else {
                    mtimerunning = false;
                    countdowntimer.cancel();
                    start.setText("start");
                    start.setBackgroundColor(Color.GREEN);
                }
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
                timeleft = starttime;
                start.setBackgroundColor(Color.GREEN);
                start.setText("Start");
                update();
            }
        }.start();
        mtimerunning =true;
        start.setBackgroundColor(Color.GRAY);
        start.setText("Pause");

    }

    public void updatebutton() {
        if (mtimerunning)
        {
            start.setBackgroundColor(Color.GRAY);
            start.setText("Pause");
        }
        else
        {
            start.setBackgroundColor(Color.GREEN);
            start.setText("Start");
        }

    }
    private void update() {
        int min = (int) (timeleft/1000)/60;
        int sec = (int) (timeleft/1000)%60;

        String t = String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        time.setText(t);
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeleft",timeleft);
        outState.putBoolean("mtimerunning",mtimerunning);
        outState.putLong("endtime",endtime);
        outState.putString("time",selectedtime.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeleft = savedInstanceState.getLong("timeleft");
        mtimerunning = savedInstanceState.getBoolean("mtimerunning");
        selectedtime.setText(savedInstanceState.getString("time"));
        updatebutton();
        update();
        if (mtimerunning)
        {
            endtime = savedInstanceState.getLong("endtime");
            timeleft = endtime - System.currentTimeMillis();
            starttimer();
        }

    }
}
