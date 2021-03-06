package com.example.anas.firstapp.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.anas.firstapp.R;
import com.example.anas.firstapp.Test;
import com.example.anas.firstapp.TestFirstActivity;
import com.example.anas.firstapp.TestNewActivity;
import com.example.anas.firstapp.TutorialActivity;
import com.example.anas.firstapp.User;

import java.util.Locale;

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        soundTouched= new MediaPlayer().create(GameActivity.this, R.raw.coin);
        soundMissed = new MediaPlayer().create(GameActivity.this, R.raw.stomp);

        lang = (String) getIntent().getSerializableExtra("LANG");

        view = new AnimationView(getApplicationContext());
        setContentView(view);
        Log.d("ANAS", "****************************************INSIDE THE RUNONUITHREAD VIEW");

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
                int d = (int) (view.getX() + view.getCircleRadius() + 15);
                int l = (int) (view.getX() - view.getCircleRadius() - 15);

                int i = (int) (event.getY());
                int j = (int) (view.getY() + view.getCircleRadius() + 15);
                int k = (int) (view.getY() - view.getCircleRadius() - 15);

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

                        } else {
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
        super.onPause();
        thread.interrupt();
        view.getLoop().interrupt();

        view.surfaceDestroyed(view.getHolder());

        Log.d("ANAS", "IS THE LOOP ALIVE  " + view.getLoop().isAlive());
        Log.d("ANAS", "IS THE TIME ALIVE  " + thread.isAlive());
        Log.d("ANAS", "THREAD STATUS: " + thread.getState());
        Log.d("ANAS", "LOOP STATUS: " + view.getLoop().getState());
    }

    public boolean retour = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            thread.interrupt();
            view.getLoop().interrupt();

            view.surfaceDestroyed(view.getHolder());

            Intent intent = new Intent(GameActivity.this, TutorialActivity.class);

            intent.putExtra("KEY", user);
            intent.putExtra("LANG", lang);
            retour = true;
            intent.putExtra("RETOUR", retour);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Log.d("ANAS", "--------------------->>>>>>PRESSED BACK BUTTON OH YEAAAHH<<<<<<<------------------");

            startActivity(intent);
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
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

        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        config.locale = new Locale(lang);
        getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }

}
