package com.android.countdowntimer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long starttime = 6000;
    TextView time;
    Button reset;
    private CountDownTimer countdowntimer;
    public Boolean mtimerunning;
    private long timeleft = starttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView);
        reset = (Button)findViewById(R.id.reset);

        starttimer();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resettime();
            }
        });
    }

    private void starttimer() {
        countdowntimer = new CountDownTimer(timeleft,1000) {
            @Override
            public void onTick(long l) {
                timeleft = l;
                update();
            }

            @Override
            public void onFinish() {

                mtimerunning = false;
            }
        }.start();
        mtimerunning =true;

    }

    private void update() {
        int min = (int) (timeleft/1000)/60;
        int sec = (int) (timeleft/1000)%60;

        String t = String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        time.setText(t);
        }

    private void resettime() {
        timeleft = starttime;
        starttimer();
    }
}
