package com.example.anas.firstapp;

import android.content.Intent;
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
                Intent intent = new Intent(SingleAdviceActivity.this, AdvicesActivity.class);
                intent.putExtra("LANG", lang);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_single_advice;
    }
}
