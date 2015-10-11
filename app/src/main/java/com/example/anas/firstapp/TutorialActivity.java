package com.example.anas.firstapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;

public class TutorialActivity extends BaseActivity {

    private User user ;
    private boolean retour = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_tutoriel);

        user = (User) getIntent().getSerializableExtra("KEY");
        retour = getIntent().getBooleanExtra("RETOUR", false);

        if(retour)
            showDialog();

        findViewById(R.id.jai_compris_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(GameActivity.class, user);
            }
        });
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_tutorial;
    }
}
