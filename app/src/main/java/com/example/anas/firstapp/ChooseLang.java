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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class ChooseLang extends AppCompatActivity{

    private RadioGroup languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lang);

        languages = (RadioGroup) findViewById(R.id.radioGroup_languages);
        //uncheck all buttons before starting to chose a langauge
        languages.clearCheck();
        languages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                if(checkedId == R.id.radio_arabic) {
                    setLocale("ar");

                } else if(checkedId == R.id.radio_french) {

                    setLocale("fr");
                } else {
                    setLocale("en");
                }
            }
        });

    }

    public void setLocale(String lang) {

        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(lang);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

        Intent welcome = new Intent(this, WelcomeActivity.class);
        welcome.putExtra("LANG", lang);

        startActivity(welcome);
        finish();
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
