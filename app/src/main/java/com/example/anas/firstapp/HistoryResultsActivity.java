package com.example.anas.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class HistoryResultsActivity extends BaseActivity {
    private User user;
    private List<Test> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_historique_resultats);

        DatabaseHandler dbHelper = DatabaseHandler.getInstance(this);

        user = (User) getIntent().getSerializableExtra("KEY");

        tests = dbHelper.findTestByUser(user.getId());

        ((TextView) findViewById(R.id.username_history_results)).setText(user.getUsername());

        String[] dates = new String[tests.size()];
        String[] scores = new String[tests.size()];

        for(int i=0; i<tests.size(); i++){
            dates[i] = tests.get(i).getCreatedAt();
            scores[i] = String.valueOf(tests.get(i).getBallTouched());
        }

        ((ListView) findViewById(R.id.list_best_score)).setAdapter(new ListResultsAdapter(dates, scores));

        findViewById(R.id.retour_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tests.isEmpty()) {
                    //go to the new profile page
                    Intent intent = new Intent(HistoryResultsActivity.this, TestNewActivity.class);
                    intent.putExtra("KEY", user);
                    intent.putExtra("LANG", lang);
                    setLocale(lang);
                    startActivity(intent);
                    finish();

                } else {
                    //go to the new reference page
                    Intent intent = new Intent(HistoryResultsActivity.this, TestFirstActivity.class);
                    intent.putExtra("KEY", user);
                    intent.putExtra("LANG", lang);
                    setLocale(lang);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.best_score;
    }

    class ListResultsAdapter extends BaseAdapter {

        String[] date;
        String[] score;

        public ListResultsAdapter(String[] username, String[] score) {
            this.date = username;
            this.score = score;
        }

        @Override
        public int getCount() {
            return date.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder ;

            if(row == null){
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.best_score_item, parent, false);
                holder = new ViewHolder();
                holder.dateTextView = (TextView) row.findViewById(R.id.text_date);
                holder.scoreTextView = (TextView) row.findViewById(R.id.text_score);
                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            holder.dateTextView.setText(date[position]);
            holder.scoreTextView.setText(score[position]);

            return row;
        }
    }

    private static class ViewHolder{
        TextView dateTextView ;
        TextView scoreTextView ;
    }
}
