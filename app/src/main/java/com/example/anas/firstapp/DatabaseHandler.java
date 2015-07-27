package com.example.anas.firstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 26/7/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "wakeup";

    //Users table name
    private static final String TABLE_NAME = "users";

    //Users table column names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENRE = "genre";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_AGE + " TEXT, " + KEY_GENRE + " TEXT"+")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //CRUD Operations

    //Create new user
    public void createUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //User username
        values.put(KEY_USERNAME, user.getUsername());
        //User age
        values.put(KEY_AGE, user.getAge());
        //User genre
        values.put(KEY_GENRE, user.getGenre());

        //Inserting row
        db.insert(TABLE_NAME, null, values);
        //close database connection
        db.close();
    }

    //Updating a user
    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_AGE, user.getAge());
        values.put(KEY_GENRE, user.getGenre());

        return db.update(TABLE_NAME, values, KEY_ID + "=?",
                        new String[]{String.valueOf(user.getId())});
    }

    //Deleting a user
    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Find a user by id
    public User findUserById(int id ){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                                new String[]{KEY_ID, KEY_USERNAME, KEY_AGE, KEY_GENRE},
                                KEY_ID+"=?",
                                new String[]{String.valueOf(id)},
                                null, null, null, null );
        if(cursor !=null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3));
        cursor.close();
        return user;
    }

    //Find a user by username
    public User findUserByName(String username ){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_ID, KEY_USERNAME, KEY_AGE, KEY_GENRE},
                KEY_USERNAME+"=?",
                new String[]{String.valueOf(username)},
                null, null, null, null );
        if(cursor !=null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));

        return user;
    }

    //Find all users
    public List<User> findAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<User> users = new ArrayList<User>();

        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        //Looping through the table and add all the users
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setAge(cursor.getString(2));
                user.setGenre(cursor.getString(3));
                //Adding user to the list
                users.add(user);
            }while(cursor.moveToNext());
        }

        return users;
    }


    //Getting users count
    public int getUsersCount(){
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }
}
