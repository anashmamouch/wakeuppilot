package com.example.anas.firstapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anas.firstapp.BaseActivity;
import com.example.anas.firstapp.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmActivity extends BaseActivity {
    //MediaPlayer mp;

    //Alarm time in milliseconds
    private static final long ALARM_TIME = 2 * 60 * 1000;
    private TextView timeText;
    public static Button alarmButton;
    String localTime = "";

    SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        alarmButton = (Button) findViewById(R.id.alarm_demarrer);

        if(sharedPreferences.getBoolean("isEnabled", true)){
            alarmButton.setEnabled(true);
        }else{
            alarmButton.setEnabled(false);
        }
        timeText = (TextView) findViewById(R.id.alarm_time_hours);

        timeText.setVisibility(View.VISIBLE);
        timeText.setText(sharedPreferences.getString("time", "00:00"));

        //timeText.setVisibility(View.INVISIBLE);

        final Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);



        //Start Alarm button pressed
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alarmButton.setEnabled(false);
                //isEnabled = false;

                editor.putBoolean("isEnabled", false);
                editor.commit();

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                cal.setTimeInMillis(cal.getTimeInMillis() + ALARM_TIME);
                Date currentLocalTime = cal.getTime();

                DateFormat date = new SimpleDateFormat("HH:mm");
                date.setTimeZone(TimeZone.getTimeZone("GMT"));

                localTime = date.format(currentLocalTime);

                editor.putString("time", localTime);
                editor.commit();

                timeText.setVisibility(View.VISIBLE);
                timeText.setText(localTime);

                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + ALARM_TIME, pendingIntent);// 20 seconds
            }
        });

        findViewById(R.id.alarm_arreter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmButton.setEnabled(true);

                editor.putBoolean("isEnabled", true);
                editor.commit();

                alarmManager.cancel(pendingIntent);

            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_alarm;
    }

    public static class AlarmReceiver extends WakefulBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.alarm);

            alarmButton.setEnabled(true);
            editor.putBoolean("isEnabled", true);
            editor.commit();

            showNotification(context);

            mp.start();
        }

        public void showNotification(Context context) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.logo_white_32)
                            .setContentTitle(context.getResources().getString(R.string.app_name))
                            .setContentText("Attention vous devez vous arreter pour vous reposer")
                            .setAutoCancel(true);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

}
