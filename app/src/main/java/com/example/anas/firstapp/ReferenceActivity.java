package com.example.anas.firstapp;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
                goToActivity(TutorialActivity.class, user);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reference;
    }

}
