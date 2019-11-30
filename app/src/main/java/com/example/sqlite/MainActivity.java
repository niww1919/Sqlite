package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.TextViewCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Calendar calendar;
    int timeNow;
    TextViewCompat textViewCompat;
    TextView textViewStart;
    TextView textViewStop;
    LinearLayoutCompat linearLayoutCompat;
    AppCompatButton buttonStart;
    AppCompatButton buttonStop;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, time INTEGER)");



        textViewStart = findViewById(R.id.tvStart);
        textViewStop = findViewById(R.id.tvStop);
        linearLayoutCompat = findViewById(R.id.ll);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);



        buttonStart.setOnClickListener(new View.OnClickListener() {//todo crash on repeat click
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                timeNow = calendar.get(Calendar.MINUTE);

                db.execSQL("INSERT INTO users VALUES ('Time now',"+ timeNow +" );");

                Cursor query = db.rawQuery("SELECT * FROM users;", null);
                if(query.moveToFirst()){
                    do{
                        String name = query.getString(0);
                        int time = query.getInt(1);
                        textViewStart.append("Name: " + name + " Time: " + time + "\n");
                    }
                    while(query.moveToNext());
                }
                query.close();
                db.close();

            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor query = db.rawQuery("SELECT * FROM users GROUP BY name;", null);
                if(query.moveToFirst()){
                    do{
                        String name = query.getString(0);
                        int time = query.getInt(1);
                        textViewStop.append("Name: " + name + " Time: " + time + "\n");
                    }
                    while(query.moveToNext());
                }
                query.close();
                db.close();


            }
        });





    }


}
