package com.example.anas.firstapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    String lang;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());


        lang  = (String) getIntent().getSerializableExtra("LANG");
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar.setNavigationIcon(R.drawable.logo_white_32);

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        //getSupportActionBar().setLogo(R.drawable.logo_white_32);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           showDialog();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected abstract int getLayoutResource();

    public void showDialog() {

        AlertDialog alert = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.quitter_application))
                .setMessage(getResources().getString(R.string.sure_vouloir_quitter))
                .setPositiveButton(getResources().getString(R.string.oui), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        moveTaskToBack(true);
                        //getParent().finish();
                        finish();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .show();
        alert.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Configuration config = new Configuration(newConfig);
        config.locale = new Locale(lang);
        getBaseContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    //Conflicts between language and orientation solved.
    public void setLocale(String lang) {
        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(lang);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

    }

    public void goToActivity(Class activity, User user){
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.putExtra("LANG", lang);

        if(user != null){
            intent.putExtra("KEY", user);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        setLocale(lang);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == android.R.id.home){
            goToActivity(WelcomeActivity.class, null);
            return true;
        }

        //Language selection
        if (id == R.id.Language) {
            startActivity(new Intent(getApplicationContext(), ChooseLang.class));
            finish();
            return true;
        }

        //Map Activity selection
        if (id == R.id.action_map) {
            goToActivity(MapActivity.class, null);
            return true;
        }

        //Advice Activity selection
        if (id == R.id.action_advice) {
            goToActivity(AdvicesActivity.class, null);
            return true;
        }

        //Relax video Activity selection
        if (id == R.id.action_video) {
            goToActivity(RelaxVideoActivity.class, null);
            return true;
        }

        //Credits Activity selection
        if (id == R.id.action_credits) {
            goToActivity(CreditsActivity.class, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
