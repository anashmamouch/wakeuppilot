package com.example.anas.firstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anas on 26/7/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 4;

    //Database Name
    private static final String DATABASE_NAME = "wakeup";

    //Users table name
    private static final String TABLE_USERS = "users";

    //Tests table name
    private static final String TABLE_TESTS = "tests";

    //Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";


    //Users table column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENRE = "genre";

    //Tests table column names
    private static final String KEY_BALL_TOUCHED = "ball_touched";
    private static final String KEY_TOTAL_TOUCHES = "total_touches";
    private static final String KEY_FIRST_TIME = "first_time";
    private static final String KEY_USER_ID = "user_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_AGE + " TEXT, "
                + KEY_GENRE + " TEXT, "
                + KEY_CREATED_AT + " DATETIME" + ")";

        String CREATE_TESTS_TABLE = "CREATE TABLE " + TABLE_TESTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_BALL_TOUCHED + " INTEGER,"
                + KEY_TOTAL_TOUCHES + " INTEGER, "
                + KEY_FIRST_TIME + " BOOLEAN, "
                + KEY_USER_ID + " INTEGER, "
                + KEY_CREATED_AT + " DATETIME, "
                + "FOREIGN KEY (" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS
                +"(" + KEY_ID + ") ON DELETE CASCADE"+")";


        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_TESTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);

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
        //User created_at
        values.put(KEY_CREATED_AT, getDateTime());

        //Inserting row
        db.insert(TABLE_USERS, null, values);
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

        return db.update(TABLE_USERS, values, KEY_ID + "=?",
                        new String[]{String.valueOf(user.getId())});
    }

    //Deleting a user
    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Find a user by id
    public User findUserById(int id ){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                                new String[]{KEY_ID, KEY_USERNAME, KEY_AGE, KEY_GENRE, KEY_CREATED_AT},
                                KEY_ID+"=?",
                                new String[]{String.valueOf(id)},
                                null, null, null, null );
        if(cursor !=null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4));
        cursor.close();
        return user;
    }

    //Find a user by username
    public User findUserByName(String username ){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_ID, KEY_USERNAME, KEY_AGE, KEY_GENRE, KEY_CREATED_AT},
                KEY_USERNAME+"=?",
                new String[]{String.valueOf(username)},
                null, null, null, null );
        if(cursor !=null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

        return user;
    }

    //Find all users
    public List<User> findAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<User> users = new ArrayList<User>();

        String query = "SELECT * FROM "+TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);

        //Looping through the table and add all the users
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setAge(cursor.getString(2));
                user.setGenre(cursor.getString(3));
                user.setCreatedAt(cursor.getString(4));

                //Adding user to the list
                users.add(user);
            }while(cursor.moveToNext());
        }

        return users;
    }


    //Getting users count
    public int getUsersCount(){
        String query = "SELECT * FROM "+TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }

    //Create a test according to a user
    public void createTest(Test test){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Test ball touched
        values.put(KEY_BALL_TOUCHED, test.getBallTouched());
        //Test total touches
        values.put(KEY_TOTAL_TOUCHES, test.getTotalTouches());
        //Test first time
        values.put(KEY_FIRST_TIME, test.getFirstTime());
        //Test user id
        values.put(KEY_USER_ID, test.getUserId());
        //Test created at
        values.put(KEY_CREATED_AT, getDateTime());

        //Inserting row in the test table
        db.insert(TABLE_TESTS, null, values);

        //Close database connection
        db.close();
    }

    //Find all tests
    public List<Test> findAllTests(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Test> tests = new ArrayList<Test>();

        String query = "SELECT * FROM "
                        + TABLE_TESTS;

        Cursor cursor = db.rawQuery(query, null);

        //Looping through the table and add all the tests
        if(cursor.moveToFirst()){
            do{
                Test test = new Test();
                test.setId(Integer.parseInt(cursor.getString(0)));
                test.setBallTouched(Integer.parseInt(cursor.getString(1)));
                test.setTotalTouches(Integer.parseInt(cursor.getString(2)));
                //test.setFirstTime(Boolean.parseBoolean(cursor.getString(3)));
                if(cursor.getInt(3) == 1)
                    test.setFirstTime(true);
                else
                    test.setFirstTime(false);
                test.setUserId(Integer.parseInt(cursor.getString(4)));
                test.setCreatedAt(cursor.getString(5));

                //Adding test to the list
                tests.add(test);
            }while(cursor.moveToNext());
        }
        db.close();
        return tests;
    }

    //Find all @tests of a specific @user
    public List<Test> findTestByUser(int userId){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Test> tests = new ArrayList<Test>();

        String query = "SELECT * FROM "
                        + TABLE_TESTS
                        + " WHERE "
                        + KEY_USER_ID
                        +" = "+ userId
                        +" ORDER BY "
                        + KEY_CREATED_AT
                        +" DESC";

        Cursor cursor = db.rawQuery(query, null);

        //Looping through the table and add tests according to the user selected
        if (cursor.moveToFirst()){
            do{
                Test test = new Test();
                test.setId(Integer.parseInt(cursor.getString(0)));
                test.setBallTouched(Integer.parseInt(cursor.getString(1)));
                test.setTotalTouches(Integer.parseInt(cursor.getString(2)));
                if(cursor.getInt(3) == 1)
                    test.setFirstTime(true);
                else
                    test.setFirstTime(false);
                test.setUserId(Integer.parseInt(cursor.getString(4)));
                test.setCreatedAt(cursor.getString(5));

                //Adding test to the list
                tests.add(test);
            }while(cursor.moveToNext());
        }
        db.close();
        return tests;
    }

    //Test Niveau de reference
    public Test niveauReferenceByUser(int userId){
        SQLiteDatabase db = this.getWritableDatabase();

        Test test = new Test();

        String query = "SELECT * FROM "
                        + TABLE_TESTS
                        + " WHERE "
                        + KEY_USER_ID
                        +" = "+ userId
                        +" AND "
                        + KEY_FIRST_TIME
                        +" = 1";
        Cursor cursor = db.rawQuery(query, null);

        test.setId(Integer.parseInt(cursor.getString(0)));
        test.setBallTouched(Integer.parseInt(cursor.getString(1)));
        test.setTotalTouches(Integer.parseInt(cursor.getString(2)));
        //test.setFirstTime(Boolean.parseBoolean(cursor.getString(3)));
        if(cursor.getInt(3) == 1)
            test.setFirstTime(true);
        else
            test.setFirstTime(false);
        test.setUserId(Integer.parseInt(cursor.getString(4)));
        test.setCreatedAt(cursor.getString(5));

        db.close();
        return  test;
    }

    //Getting  tests count
    public int getTestsCount(){
        String query = "SELECT * FROM "+TABLE_TESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }

    //Created_at value current datetime
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
