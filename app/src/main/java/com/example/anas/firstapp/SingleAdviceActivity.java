package com.example.anas.firstapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class SingleAdviceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title_toolbar;

    private String lang;

    /*JSON Node names*/
    public static String TAG_TITLE = "title";
    public static String TAG_BODY = "body";
    public static String TAG_DATE = "created_at";

    private String title;
    private String body;
    private String date;

    private TextView titleTextView;
    private TextView bodyTextView;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_advice);

        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title_toolbar = (TextView) findViewById(R.id.toolbar_title);

        lang  = (String) getIntent().getSerializableExtra("LANG");


        title_toolbar.setText(R.string.toolbar_conseils);

        toolbar.setNavigationIcon(R.drawable.logo_white_32);

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        //getSupportActionBar().setLogo(R.drawable.logo_white_32);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = (String) getIntent().getSerializableExtra(TAG_TITLE);
        body = (String) getIntent().getSerializableExtra(TAG_BODY);
        date = (String) getIntent().getSerializableExtra(TAG_DATE);

        titleTextView = (TextView) findViewById(R.id.title_label);
        bodyTextView = (TextView) findViewById(R.id.body_label);
        dateTextView = (TextView) findViewById(R.id.date_label);

        titleTextView.setText(title);
        bodyTextView.setText(body);
        dateTextView.setText(date);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        newConfig.locale = new Locale(lang);
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
        super.onConfigurationChanged(newConfig);

    }

    //Conflicts between language and orientation solved.
    public void setLocale(String lang) {

        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(lang);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //Language selection
        if (id == R.id.Language) {
            startActivity(new Intent(getApplicationContext(), ChooseLang.class));
            finish();
            return true;
        }

        //Map Activity selection
        if (id == R.id.action_map) {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.putExtra("LANG", lang);
            startActivity(intent);
            //finish();
            return true;
        }

        //Advice Activity selection
        if (id == R.id.action_advice) {
            Intent intent = new Intent(getApplicationContext(), AdvicesActivity.class);
            intent.putExtra("LANG", lang);
            startActivity(intent);
            //finish();
            return true;
        }

        //Credits Activity selection
        if (id == R.id.action_credits) {
            Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
            intent.putExtra("LANG", lang);
            startActivity(intent);
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
