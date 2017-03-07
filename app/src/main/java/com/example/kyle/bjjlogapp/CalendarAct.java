package com.example.kyle.bjjlogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

public class CalendarAct extends AppCompatActivity {

    CalendarView calendar;
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendaract);

        myDb = new DatabaseHelper(this);

        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int correctMonth = month+1;
                Intent intent = new Intent(CalendarAct.this, TrainingLogDate.class);
                intent.putExtra("Date", dayOfMonth + "/" + correctMonth + "/" + year);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_stats) {
            if (myDb.doesDatabaseExist(this) == true) {
                startActivity(new Intent(this, StatsActivity.class));
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, mFragment).commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
