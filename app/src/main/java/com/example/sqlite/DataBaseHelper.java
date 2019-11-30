package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "timestore.dbOne";
    public static final int VERSION = 1;
    public static final String TABLE = "time";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATA = "_data";
    public static final String COLUMN_TIME = "_time";



    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE time ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATA + " TEXT, "
                + COLUMN_TIME + " TEXT);");

//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_DATA + ", " + COLUMN_TIME  + ") VALUES ('23', '15');");
        db.execSQL("INSERT INTO "+ TABLE +" VALUES ('23', '15');");
//        db.execSQL("INSERT INTO users VALUES ('Time now'," + timeNow + " );");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);

    }
}
