package com.example.anas.firstapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.Locale;

public class ChooseLang extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lang);

        RadioGroup languages = (RadioGroup) findViewById(R.id.radioGroup_languages);
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
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("LANG", lang);
        startActivity(intent);
        finish();
    }

}
