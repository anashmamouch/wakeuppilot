package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class AfterReferenceActivity extends BaseActivity {

    private User user ;
    private List<Test> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_niveau_reference);

        DatabaseHandler dbHelper = DatabaseHandler.getInstance(this);
        user = (User) getIntent().getSerializableExtra("KEY");
        tests = dbHelper.findTestByUser(user.getId());

        findViewById(R.id.retour_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the test
                Intent intent = new Intent(getApplicationContext(), TestNewActivity.class);
                Log.d("BENZINO", "creating the intent");
                intent.putExtra("KEY", user);
                intent.putExtra("TESTS", (Serializable) tests);
                intent.putExtra("LANG", lang);
                setLocale(lang);
                Log.d("BENZINO", "starting the intent");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_after_reference;
    }
}
