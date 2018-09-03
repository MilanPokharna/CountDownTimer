package com.android.countdowntimer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    public TextView tdate,time;
    public Button bdate,btime,bnext,send,timer;
    String d="",t="",string="",t2="";
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("quiz");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        tdate = (TextView)findViewById(R.id.textdate);
        time = (TextView)findViewById(R.id.texttime);
        bdate = (Button)findViewById(R.id.buttondate);
        btime = (Button)findViewById(R.id.buttontime);
        bnext = (Button)findViewById(R.id.next);
        send = (Button)findViewById(R.id.buttonsend);

        btime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                final int hours = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Main2Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedhour, int selectedminute) {
                        time.setText(""+selectedhour+"H:"+selectedminute+"M:00S");
                        t2 =""+selectedhour+"H:"+selectedminute+"M:00S";
                        t = ""+selectedhour+":"+selectedminute+":00";
                    }
                },hours,minute,false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        bdate.setOnClickListener(new View.OnClickListener() {
            Calendar mcurrentTime = Calendar.getInstance();
            final int date = mcurrentTime.get(Calendar.DATE);
            final int mon = mcurrentTime.get(Calendar.MONTH);
            final int year = mcurrentTime.get(Calendar.YEAR);
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Main2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        tdate.setText(""+i2+"-"+i1+"-"+i);
                        d=""+i2+"-"+i1+"-"+i;
                    }
                },year,mon,date);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!(t2.isEmpty()) && !(d.isEmpty()))
                {
                    string = d+" "+t;
                    if (isNetworkConnected())
                    {
                        ref.child("1").child("quiztime").setValue(string).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Main2Activity.this, "time uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(Main2Activity.this, "no internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Main2Activity.this, "Please select date and time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void timer(View view) {
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("t2",t2);
        editor.putString("t",t);
        editor.putString("d",d);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        t2 = prefs.getString("t2","");
        t = prefs.getString("t","");
        d = prefs.getString("d","");
        tdate.setText(d);
        time.setText(t2);
    }
}
