package com.example.anas.firstapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ProfilesActivity extends BaseActivity {

    private DatabaseHandler dbHelper;

    private List<User> users;
    private List<Test> tests;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.toolbar_title)).setText(R.string.toolbar_liste_profiles);

        findViewById(R.id.nouveau_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(NewProfileActivity.class, null);
            }
        });

        dbHelper = DatabaseHandler.getInstance(this);

        users = dbHelper.findAllUsers();
        String[] names = new String[users.size()];
        String[] ages = new String[users.size()];
        String[] genres = new String[users.size()];

        for(int i=0; i<users.size(); i++){
            names[i] = users.get(i).getUsername();
            ages[i] = users.get(i).getAge();
            genres[i] = users.get(i).getGenre();
        }

        ((ListView)findViewById(R.id.listView_profiles)).setAdapter(new ListProfilesAdapter(names, ages, genres));

        ((ListView)findViewById(R.id.listView_profiles)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                user = users.get(position);
                tests = dbHelper.findTestByUser(user.getId());

                //If the user already passed the test
                if (!tests.isEmpty()) {
                    goToActivity(TestNewActivity.class, user);
                } else {
                    goToActivity(TestFirstActivity.class, user);
                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profiles;
    }

     class ListProfilesAdapter extends BaseAdapter {

         String[] username;
         String[] age;
         String[] genre;

        public ListProfilesAdapter(String[] username, String[] age, String[] genre) {
            this.username = username;
            this.age = age;
            this.genre = genre;
        }

        @Override
        public int getCount() {
            return username.length;
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
            ViewHolder holder;

            if(row == null){
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.listview_item, parent, false);

                holder = new ViewHolder();

                holder.usernameTextView = (TextView) row.findViewById(R.id.textView1);
                holder.ageTextView = (TextView) row.findViewById(R.id.textView2);
                holder.avatar = (ImageView) row.findViewById(R.id.avatar);
                holder.delete = (ImageView) row.findViewById(R.id.delete_profile);

                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }

            String ageString;
            final String usernameString = username[position];
            holder.usernameTextView.setText(usernameString);

            final DatabaseHandler dbHandler = DatabaseHandler.getInstance(getApplicationContext());

            holder.delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   final AlertDialog alert = new AlertDialog.Builder(ProfilesActivity.this)
                           .setTitle(R.string.supprimer_profile)
                           .setMessage(R.string.sure_supprimer_profile)
                           .setPositiveButton(R.string.non, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   // redo the test
                                   dialog.dismiss();
                               }
                           })
                           .setNegativeButton(R.string.oui, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   dbHandler.deleteUser(dbHandler.findUserByName(usernameString));

                                   goToActivity(ProfilesActivity.class, null);
                                   dialog.dismiss();
                               }
                           })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();
                   alert.setCanceledOnTouchOutside(false);
               }
           });

            if (age[position].equals("- 40 ans")) {
                ageString = getResources().getString(R.string.moins_40);

                if(genre[position].equals("Femme"))
                    holder.avatar.setImageResource(R.drawable.avatar_female_40);
                else if(genre[position].equals("Homme"))
                    holder.avatar.setImageResource(R.drawable.avatar_male_40);

            }else if(age[position].equals("40 - 60 ans")){
                ageString = getResources().getString(R.string.entre_40_60);

                if(genre[position].equals("Femme"))
                    holder.avatar.setImageResource(R.drawable.avatar_female_40_60);
                else if(genre[position].equals("Homme"))
                    holder.avatar.setImageResource(R.drawable.avatar_male_40_60);

            }else if(age[position].equals("+ 60 ans")) {
                ageString = getResources().getString(R.string.plus_60);

                if(genre[position].equals("Femme"))
                    holder.avatar.setImageResource(R.drawable.avatar_female_60);
                else if(genre[position].equals("Homme"))
                    holder.avatar.setImageResource(R.drawable.avatar_male_60);

            }else{
                ageString = "NULL";
                holder.avatar.setImageResource(R.drawable.avatar_unknown);
            }

            holder.ageTextView.setText(ageString);
            return row;
        }
    }

    private static class ViewHolder {
        TextView usernameTextView;
        TextView ageTextView;
        ImageView avatar;
        ImageView delete;
    }
}
