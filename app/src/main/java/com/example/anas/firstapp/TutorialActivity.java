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
                //go to the test
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("KEY", user);
                intent.putExtra("LANG", lang);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                setLocale(lang);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_tutorial;
    }
}
