package com.example.anas.firstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class ChooseLang extends AppCompatActivity{

    private Toolbar toolbar;
    private TextView title;
    private Button createNewProfile;
    private Spinner spinner;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lang);
        //Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.toolbar_title);
        createNewProfile = (Button) findViewById(R.id.nouveau_profile);
        spinner = (Spinner) findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 1) {

                    Toast.makeText(parent.getContext(),
                            "You have selected English", Toast.LENGTH_SHORT)
                            .show();
                    setLocale("en");
                } else if (pos == 2) {

                    Toast.makeText(parent.getContext(),
                            "You have selected French", Toast.LENGTH_SHORT)
                            .show();
                    setLocale("fr");
                } else if (pos == 3) {

                    Toast.makeText(parent.getContext(),
                            "You have selected Arabic", Toast.LENGTH_SHORT)
                            .show();
                    setLocale("ar");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        title.setText("WAKEUPPILOT");

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;

        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, WelcomeActivity.class);
        refresh.putExtra("LANG", lang);
        startActivity(refresh);
    }

    protected void setCurrentLanguage() {
        Configuration configuration = new Configuration(getResources().getConfiguration());
        SharedPreferences preferences = getSharedPreferences("SETTINGS_FILE", Context.MODE_PRIVATE);
        String restoredLanguage = preferences.getString("currentLanguage", null);
        if (restoredLanguage.equalsIgnoreCase("en")) {
            configuration.locale = Locale.ENGLISH;
            getResources().updateConfiguration(configuration, null);
        }
        if (restoredLanguage.equalsIgnoreCase("fr")) {
            configuration.locale = Locale.FRENCH;
            getResources().updateConfiguration(configuration, null);
        }
        if (restoredLanguage.equalsIgnoreCase("ar")) {
            //configuration.locale = Locale.;
            getResources().updateConfiguration(configuration, null);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_lang, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
