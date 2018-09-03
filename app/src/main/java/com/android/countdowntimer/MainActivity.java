package com.android.countdowntimer;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    TextView remtime;
    private CountDownTimer countdowntimer;
    public Boolean mtimerunning = false;
    private long timeleft = 0;
    private long endtime;
    public String ttt;
    public int mon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView);
        selectedtime= (TextView)findViewById(R.id.selecttime);
        remtime = (TextView)findViewById(R.id.counttime);
        update();
        start = (Button)findViewById(R.id.reset);
        select = (Button)findViewById(R.id.select);
        start.setBackgroundColor(Color.GREEN);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);

                final int date = mcurrentTime.get(Calendar.DATE);
                mon = mcurrentTime.get(Calendar.MONTH);
                final int year = mcurrentTime.get(Calendar.YEAR);
                mon=mon+1;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String h="",m="";
                        String h2="",m2="";

                        int hour = selectedHour;
                        int minutes = selectedMinute;
                        int hour2 = selectedHour;
                        int minutes2 = selectedMinute;

                        if (minutes2 < 10)
                            m2 = "0" + minutes2 ;
                        else
                            m2 = ""+minutes2;
                        if (hour2 < 10)
                            h2 = "0" + hour2 ;
                        else
                            h2 = ""+hour2;

                        if (minutes2 < minute)
                        {
                            minutes2 = minutes2 + 60 - minute;

                            hour2 = hour2-1;
                            if (hour2 < hours)
                            {
                                hour2 = hour2 + 24 - hours;
                            }
                            else
                                hour2 = hour2 - hours;
                        }
                        else {
                            minutes2 = minutes2 - minute;
                            if (hour2 < hours)
                            {
                                hour2 = hour2 + 24 - hours;
                            }
                            else
                                hour2 = hour2 - hours;
                        }
//                        Toast.makeText(MainActivity.this, "hour of the day :"+hours, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "hour selected :"+hour, Toast.LENGTH_SHORT).show();


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
                        remtime.setVisibility(View.VISIBLE);

                        if (minutes2 < 10)
                            m = "0" + minutes2 ;
                        else
                            m = ""+minutes2;
                        if (hour2 < 10)
                            h = "0" + hour2 ;
                        else
                            h = ""+hour2;
                        remtime.setText("DownCounter Time \n "+h+"H : "+m+"M");
//                        Toast.makeText(MainActivity.this, "date :"+date+"-"+mon+"-"+year+" "+h2+":"+m2+":00", Toast.LENGTH_SHORT).show();
                        ttt = +date+"-"+mon+"-"+year+" "+h2+":"+m2+":00";
                        gettime();
                    }
                }, hours, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mtimerunning){
                    if (timeleft == 0)
                        timeleft = 6000;
                    starttimer();
                }
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
                timeleft = 0;
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
        int hour = (int) (timeleft/1000)/60/60;
        int min = (int) (timeleft/1000)/60;
        int sec = (int) (timeleft/1000)%60;

        String t = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,min,sec);
        time.setText(t);
        }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timeleft",timeleft);
        editor.putBoolean("mtimerunning",mtimerunning);
        editor.putLong("endtime",endtime);
        editor.putString("time",selectedtime.getText().toString());
        editor.apply();

        if (countdowntimer != null)
        {
            countdowntimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        timeleft = prefs.getLong("timeleft",starttime);
        mtimerunning = prefs.getBoolean("mtimerunning",false);
        selectedtime.setText(prefs.getString("time","00:00"));

        if (mtimerunning)
        {
            endtime = prefs.getLong("endtime",0);
            timeleft = endtime - System.currentTimeMillis();
            if (timeleft < 0) {
                mtimerunning = false;
                timeleft = 0;
                updatebutton();
                update();
            }
            else
                starttimer();
        }
    }

    public void gettime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        String dateString = "22-03-2017 11:18:32";
//        ttt="02-09-2018 18:00:00";
        try{
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(ttt);
            Calendar calendar = Calendar.getInstance();
//            Toast.makeText(this, "Given Time in milliseconds : "+date.getTime(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Current time :"+System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Current time2 :"+calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();
            timeleft = date.getTime() - System.currentTimeMillis();
            starttimer();
            //Setting the Calendar date and time to the given date and time
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
}
