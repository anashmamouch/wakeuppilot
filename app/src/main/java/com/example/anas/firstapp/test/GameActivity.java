package com.example.anas.firstapp.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.anas.firstapp.Test;
import com.example.anas.firstapp.User;

/**
 * Created by Anas on 30/7/15.
 */
public class GameActivity extends Activity{

    private AnimationView view;
    private Test test;
    private User user;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        view = new AnimationView(this);
        user = (User) getIntent().getSerializableExtra("KEY");

        thread = new Thread() {

            @Override
            public void run() {
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

                                view.alertDialog(user);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();


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
                        view.setX(event.getX());
                        view.setY(event.getY());
                        view.setTouching(true);
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
                view.invalidate();
                return true;
            }


        });
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
        view.getLoop();

        view.surfaceDestroyed(view.getHolder());
        Log.d("ANAS", "IS THE LOOP ALIVE" + view.getLoop().isAlive());
        Log.d("ANAS", "IS THE TIME ALIVE" + thread.isAlive());
        Log.d("ANAS", "THREAD STATUS: " + thread.getState());
        Log.d("ANAS", "LOOP STATUS: " + view.getLoop().getState());

    }
}
