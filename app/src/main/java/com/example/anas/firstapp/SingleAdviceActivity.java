package com.example.anas.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleAdviceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_conseils);

        ((TextView) findViewById(R.id.title_label)).setText(((String) getIntent().getSerializableExtra("title")));
        ((TextView) findViewById(R.id.body_label)).setText(((String) getIntent().getSerializableExtra("body")));
        ((TextView) findViewById(R.id.date_label)).setText(((String) getIntent().getSerializableExtra("created_at")));

        findViewById(R.id.retour_conseils).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(AdvicesActivity.class, null);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_single_advice;
    }
}
