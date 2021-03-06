package com.example.anas.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (lang) {
            case "ar":
                ((ImageView) findViewById(R.id.logo_welcome)).setImageResource(R.drawable.logo_teal_marhaba);
                break;
            case "fr":
                ((ImageView) findViewById(R.id.logo_welcome)).setImageResource(R.drawable.logo_teal_bienvenue);
                break;
            case "en":
                ((ImageView) findViewById(R.id.logo_welcome)).setImageResource(R.drawable.logo_teal_welcome);
                break;
            default: break;
        }

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_bienvenue);

        findViewById(R.id.nouveau_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(NewProfileActivity.class, null);
            }
        });
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcome;
    }

}
