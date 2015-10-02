package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.anas.firstapp.test.GameActivity;

import java.util.Locale;

public class ReferenceActivity extends BaseActivity {
    private User user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("KEY");

        ((TextView)findViewById(R.id.toolbar_title)).setText(R.string.toolbar_niveau_reference);

        findViewById(R.id.passer_niveau_reference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the tutorial
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                intent.putExtra("KEY", user);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reference;
    }

}
