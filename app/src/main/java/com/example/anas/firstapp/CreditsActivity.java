package com.example.anas.firstapp;

import android.os.Bundle;
import android.widget.TextView;

public class CreditsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_credits);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_credits;
    }
}
