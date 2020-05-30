package com.kelompokmcs.tournal.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "tournal.db";
    public static int DB_VERSION = 1;
    public static String TABLE_GROUP = "grouptable";
    public static String TABLE_AGENDA = "agenda";
    public static String TABLE_ANNOUNCEMENT = "announcement";
    public static String COL_GROUP_ID = "group_id";
    public static String COL_GROUP_CODE = "group_code";
    public static String COL_GROUP_NAME = "group_name";
    public static String COL_GROUP_LOCATION = "group_location";
    public static String COL_START_DATE = "start_date";
    public static String COL_END_DATE = "end_date";
    public static String COL_GROUP_PASS = "group_pass";
    public static String COL_AGENDA_ID = "agenda_id";
    public static String COL_AGENDA_TITLE = "agenda_title";
    public static String COL_AGENDA_DESC = "agenda_desc";
    public static String COL_START_TIME = "start_time";
    public static String COL_END_TIME = "end_time";
    public static String COL_LAT = "lat";
    public static String COL_LNG = "lng";
    public static String COL_ALT = "alt";
    public static String COL_DATE_AND_TIME = "date_and_time";
    public static String COL_ANNOUNCEMENT_ID = "announcement_id";
    public static String COL_ANNOUNCEMENT_TITLE = "announcement_title";
    public static String COL_ANNOUNCEMENT_DESC = "announcement_desc";
    public static String COL_USER_NAME = "user_name";
    public static String COL_USER_EMAIL = "user_email";
    public static String COL_USER_PHOTO = "user_photo";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_GROUP + " ( "+
                    COL_GROUP_ID + " INTEGER PRIMARY KEY, "+
                    COL_GROUP_CODE + " VARCHAR(5), "+
                    COL_GROUP_NAME + " TEXT, "+
                    COL_GROUP_LOCATION + " TEXT, "+
                    COL_START_DATE + " DATE, "+
                    COL_END_DATE + " DATE, "+
                    COL_GROUP_PASS + " TEXT );");

        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_AGENDA + " ( "+
                    COL_AGENDA_ID + " INTEGER PRIMARY KEY, "+
                    COL_GROUP_ID + " INTEGER, " +
                    COL_AGENDA_TITLE + " TEXT, " +
                    COL_AGENDA_DESC + " TEXT, " +
                    COL_LAT + " REAL, " +
                    COL_LNG + " REAL, " +
                    COL_ALT + " REAL, " +
                    COL_START_TIME + " DATETIME, " +
                    COL_END_TIME + " DATETIME, " +
                    "FOREIGN KEY (" + COL_GROUP_ID + ") REFERENCES " + TABLE_AGENDA + "(" + COL_GROUP_ID + "));");

        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_ANNOUNCEMENT + " ( "+
                COL_ANNOUNCEMENT_ID + " INTEGER PRIMARY KEY, "+
                COL_GROUP_ID + " INTEGER, " +
                COL_ANNOUNCEMENT_TITLE + " TEXT, " +
                COL_ANNOUNCEMENT_DESC+ " TEXT, " +
                COL_DATE_AND_TIME + " DATETIME, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_USER_PHOTO + " TEXT, " +
                "FOREIGN KEY (" + COL_GROUP_ID + ") REFERENCES " + TABLE_AGENDA + "(" + COL_GROUP_ID + "));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ANNOUNCEMENT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_AGENDA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_GROUP);
        onCreate(sqLiteDatabase);
    }
}
