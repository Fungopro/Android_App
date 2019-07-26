package com.example.fungo.shrek;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.lang3.time.StopWatch;

import static java.lang.String.*;


public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final StopWatch timer = new StopWatch();
        final String filename = "android.resource://" + this.getPackageName() + "/raw/sound.mp3";
        final MediaPlayer mpp = MediaPlayer.create(this, R.raw.sound); //mp3 file in res/raw folder
        mpp.setLooping(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



//            Button btnpause = (Button) findViewById(R.id.music); //Pause
//            btnpause.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View vtwo) {
//                    if (mpp.isPlaying()) {
//                        mpp.pause();
//                        mpp.seekTo(0);
//                    }
//                }
//            });
        final ImageButton start_btn = (ImageButton) findViewById(R.id.start);
        ImageButton stop_btn = (ImageButton) findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                timer.reset();
                mpp.seekTo(0);
                mpp.pause();
                start_btn.setImageResource(R.drawable.start);

            }
        });


        start_btn.setOnClickListener(new View.OnClickListener() {
            boolean pause = false;
            boolean start = false;
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                TextView text = (TextView) findViewById(R.id.text);
                @SuppressLint("SetTextI18n")
                @Override
                public void handleMessage(Message msg) {

                    text.setText(timer.toString().split(valueOf(':'))[1]+" : "+timer.toString().split(valueOf(':'))[2].substring(0,2));
                }
            };

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                Runnable run = new Runnable() {
                    @Override
                    public void run() {

                        synchronized (this) {
                            try {
                                do {
                                    handler.sendEmptyMessage(0);
                                    wait(100);
                                } while (true);
                            } catch (Exception ignored) {
                            }
                        }
                    }

                };
                if(!mpp.isPlaying()){
                    mpp.start();
                }else{

                    mpp.pause();
                }
                if(!timer.isStarted()){
                    Thread thread = new Thread(run);
                    thread.start();
                    timer.start();
                    start=!start;
                    start_btn.setImageResource(R.drawable.pause);

                }
                else
                if (!timer.isSuspended()) {
                    if (timer.isStarted()) {
                        timer.suspend();
                        pause = true;
                        start_btn.setImageResource(R.drawable.start);
                    } else {
                        timer.start();
                        pause = false;
                    }
                }else
                {
                    if (timer.isSuspended()) {
                        timer.resume();
                        pause = false;
                        start_btn.setImageResource(R.drawable.pause);
                    } else {
                        timer.start();
                        pause = false;
                    }
                }
            }
        });

    }

}