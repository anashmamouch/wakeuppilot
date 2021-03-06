package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TestFirstActivity extends BaseActivity {

    private User user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("KEY");

        String username = user.getUsername();
        String age = user.getAge();

        if(age.equals("- 40 ans")){
            age = getResources().getString(R.string.moins_40);
        }else if(age.equals("40 - 60 ans")){
            age = getResources().getString(R.string.entre_40_60);
        } else if(age.equals("+ 60 ans")){
            age = getResources().getString(R.string.plus_60);
        }

        if(lang.equals("ar"))
            ((TextView) findViewById(R.id.toolbar_title)).setText(age + " | " + username);
        else
            ((TextView) findViewById(R.id.toolbar_title)).setText(username + " | " + age);

        findViewById(R.id.niveau_reference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ReferenceActivity.class, user);
            }
        });

        findViewById(R.id.retour_list_profiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(ProfilesActivity.class, null);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_first_test;
    }

}
