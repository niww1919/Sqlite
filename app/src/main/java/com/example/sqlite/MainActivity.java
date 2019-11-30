package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.TextViewCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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
    SQLiteDatabase dbOne;
    SQLiteDatabase dbTwo;

    TextView dbHeader;
    ListView dbList;
    DataBaseHelper dataBaseHelperOne;
    DataBaseHelper dataBaseHelperTwo;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOne = getBaseContext().openOrCreateDatabase("app.dbOne", MODE_PRIVATE, null);
        dbOne.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, time INTEGER)");


        textViewStart = findViewById(R.id.tvStart);
        textViewStop = findViewById(R.id.tvStop);
        linearLayoutCompat = findViewById(R.id.ll);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);

        dbHeader = findViewById(R.id.dbHeader);
        dbList = findViewById(R.id.dbList);
        dataBaseHelperTwo = new DataBaseHelper(getApplicationContext()); //todo what is context??


        buttonStart.setOnClickListener(new View.OnClickListener() {//todo crash on repeat click
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                timeNow = calendar.get(Calendar.MINUTE);

                dbOne.execSQL("INSERT INTO users VALUES ('Time now'," + timeNow + " );");

                Cursor query = dbOne.rawQuery("SELECT * FROM users;", null);
                if (query.moveToFirst()) {
                    do {
                        String name = query.getString(0);
                        int time = query.getInt(1);
                        textViewStart.append("Name: " + name + " Time: " + time + "\n");
                    }
                    while (query.moveToNext());
                }
                query.close();
                dbOne.close();

            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor query = dbOne.rawQuery("SELECT * FROM users GROUP BY name;", null);
                if (query.moveToFirst()) {
                    do {
                        String name = query.getString(0);
                        int time = query.getInt(1);
                        textViewStop.append("Name: " + name + " Time: " + time + "\n");
                    }
                    while (query.moveToNext());
                }
                query.close();
                dbOne.close();


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        dbTwo = dataBaseHelperTwo.getReadableDatabase();
        //получаем данные из бд в виде курсора
//        userCursor = dbTwo.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE + ";", null);
        userCursor =  dbTwo.rawQuery("select * from "+ DataBaseHelper.TABLE+";", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{DataBaseHelper.COLUMN_TIME, DataBaseHelper.COLUMN_DATA};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        dbHeader.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        dbList.setAdapter(userAdapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        dbTwo.close();
        userCursor.close();
    }
}
