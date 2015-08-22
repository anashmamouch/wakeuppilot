package com.example.anas.firstapp.test;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.anas.firstapp.R;
import com.example.anas.firstapp.Test;
import com.example.anas.firstapp.User;

import java.util.Locale;

/**
 * Created by Anas on 30/7/15.
 */
public class GameActivity extends Activity{

    private AnimationView view;
    private Test test;
    private User user;
    private String lang;
    private MediaPlayer soundTouched;
    private MediaPlayer soundMissed;
    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */

    private Thread thread;

    /*TODO OnBackPressed */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        soundTouched= new MediaPlayer().create(GameActivity.this, R.raw.coin);
        soundMissed = new MediaPlayer().create(GameActivity.this, R.raw.stomp);

        lang = (String) getIntent().getSerializableExtra("LANG");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view = new AnimationView(getApplicationContext());
                setContentView(view);
                Log.d("ANAS", "****************************************INSIDE THE RUNONUITHREAD VIEW");


            }
        });


        //view = new AnimationView(getApplicationContext());

        user = (User) getIntent().getSerializableExtra("KEY");


        thread = new Thread() {

            @Override
            public void run() {
                Log.d("ANAS", "****************************************STARTING TIME THREAD");
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.incrementTime(1);
                                //Log.d("ANAS", "****************************************INSIDE THE TIME RUN VIEW");

                                if(view.getTime() %10 == 0){
                                    Log.d("ANAS", "TIME: " + view.getTime() + " seconds...");
                                    Log.d("ANAS", "--------Ball Touched: " + view.getTouched() + "times-------");
                                }

                                view.alertDialog(user, GameActivity.this, lang);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                }
            }
        };

        Log.d("ANAS", "VISIBILITY " + view.getVisibility());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int h = (int) (event.getX());
                int d = (int) (view.getX() + view.getCircleRadius());
                int l = (int) (view.getX() - view.getCircleRadius());

                int i = (int) (event.getY());
                int j = (int) (view.getY() + view.getCircleRadius());
                int k = (int) (view.getY() - view.getCircleRadius());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (l < h && h < d && k < i && i < j) {
                            Log.d("ANAS", "------------------->>TOUCHED");
                            view.setX(event.getX());
                            view.setY(event.getY());
                            view.increaseSpeed(0.5F);
                            view.changePosition();
                            //sound
                            soundTouched.start();

                        }else{
                            soundMissed.start();
                        }

                        view.incrementTouch();
                        view.success();
                        view.setTouching(true);
                        Log.d("ANAS", "------------------->>ACTION DOWN");
                        break;

                    case MotionEvent.ACTION_UP:
                        //finger touches the screen


                        view.setTouching(false);
                        Log.d("ANAS", "------------------->>ACTION UP");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //finger move in the screen (drag)
                        //view.setX(event.getX());
                        //view.setY(event.getY());
                        //view.setTouching(true);
                        Log.d("ANAS", "------------------->>ACTION MOVE");
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        //finger presses outside the dialog box
                        Log.d("ANAS", "*********ACTION OUTSIDE********");
                        break;

                    default:
                        view.setTouching(false);
                        Log.d("ANAS", "------------------->>DEFAULT");

                }

                //view.invalidate();
                return true;
            }

        });

        //view.surfaceCreated(view.getHolder());
        thread.start();
    }

    @Override
    protected void onPause() {

        thread.interrupt();
        view.getLoop();

        view.surfaceDestroyed(view.getHolder());

        super.onPause();
        //setContentView(null);
        finish();

        Log.d("ANAS", "IS THE LOOP ALIVE  " + view.getLoop().isAlive());
        Log.d("ANAS", "IS THE TIME ALIVE  " + thread.isAlive());
        Log.d("ANAS", "THREAD STATUS: " + thread.getState());
        Log.d("ANAS", "LOOP STATUS: " + view.getLoop().getState());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        thread.interrupt();
        view.surfaceDestroyed(view.getHolder());
        finish();

    }
    /*TODO FIX BUG WHEN CHANGING ORIENTATION BALL CROSSES THE EDGES */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        view.setWidth(width);
        view.setHeight(height);

        newConfig.locale = new Locale(lang);
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
        super.onConfigurationChanged(newConfig);

    }


    private class DisplayTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

    }
}
