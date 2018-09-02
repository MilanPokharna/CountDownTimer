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
    Button start;
    private CountDownTimer countdowntimer;
    public Boolean mtimerunning = false;
    private long timeleft = starttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView);
        update();
        start = (Button)findViewById(R.id.reset);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mtimerunning)
                    starttimer();
                else {
                    mtimerunning = false;
                    countdowntimer.cancel();
                    start.setText("start");
                }
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
                timeleft = starttime;
                start.setText("Reset");
                update();
            }
        }.start();
        mtimerunning =true;
        start.setText("Pause");

    }

    private void update() {
        int min = (int) (timeleft/1000)/60;
        int sec = (int) (timeleft/1000)%60;

        String t = String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        time.setText(t);
        }

}
